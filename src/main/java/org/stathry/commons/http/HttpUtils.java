package org.stathry.commons.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stathry.commons.utils.ApplicationContextUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * HttpUtils
 * Created by dongdaiming on 2018-11-22 18:36
 */
public class HttpUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(PoolingHttpClientBuilder.class);

    private static PoolingHttpClientBuilder poolingHttpClientBuilder;

    private static final String COMMON_USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36;rokey.me/0.1";
    private static final int DEFAULT_SOCKET_TIMEOUT = 10000;

    public static PoolingHttpClientBuilder getPoolingHttpClientBuilder() {
        if(poolingHttpClientBuilder == null) {
            synchronized (HttpUtils.class) {
                if(poolingHttpClientBuilder == null) {
                    try {
                        poolingHttpClientBuilder = ApplicationContextUtils.getBean(PoolingHttpClientBuilder.class);
                    } catch (Exception e) {}
                }
                if(poolingHttpClientBuilder == null) {
                    poolingHttpClientBuilder = new PoolingHttpClientBuilder();
                }
            }
        }
      return poolingHttpClientBuilder;
    }

    public static HttpClient getHttpClient() {
        return getPoolingHttpClientBuilder().getHttpClient();
    }

    public static String urlEncode(final List<? extends NameValuePair> parameters) {
        return URLEncodedUtils.format(parameters, "utf-8");
    }

    public static String urlEncode(final List<? extends NameValuePair> parameters, final String charset) {
        return URLEncodedUtils.format(parameters, charset);
    }

    public static String urlEncode(String str) {
        return urlEncode(str, "utf-8");
    }

    public static String urlEncode(String str, String enc) {
        String es = "";
        try {
            es = URLEncoder.encode(str, enc);
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("unsupported encoding " + enc, e);
        }
        return es;
    }

    /**
     * postJSONString
     * @param uri
     * @param requestBody
     * @return
     */
    public static String postJSONString(String uri, String requestBody) {
        return postJSONString(uri, requestBody, DEFAULT_SOCKET_TIMEOUT);
    }

    /**
     * postJSONString
     * @param uri
     * @param requestBody
     * @param timeout millis
     * @return
     */
    public static String postJSONString(String uri, String requestBody, int timeout) {
        LOGGER.debug("invoke uri {} with params {}.", uri, requestBody);
        HttpPost httpPost = new HttpPost(uri);
        httpPost.addHeader(
                "User-Agent", COMMON_USER_AGENT);
        httpPost.addHeader("Content-Type", ContentType.APPLICATION_JSON.getMimeType());

        RequestConfig config = RequestConfig.custom().setSocketTimeout(timeout).build();
        httpPost.setConfig(config);
        HttpResponse response = null;
        try {
            HttpEntity entity = new StringEntity(requestBody, "utf-8");
            httpPost.setEntity(entity);
            HttpClient client = getHttpClient();
            response = client.execute(httpPost);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            LOGGER.info("invoke uri {}, response status {}, reason {}.", uri, statusCode, statusLine.getReasonPhrase());
            if (statusCode == HttpStatus.SC_OK) {
                HttpEntity respEntity = response.getEntity();
                String str = EntityUtils.toString(respEntity, "utf-8");
                LOGGER.debug("invoke uri {}, response body: {}", uri, str);
                return str;
            }
        } catch (Exception e) {
            LOGGER.error("invoke uri " + uri + " error, with params " + requestBody, e);
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (Exception e) {
                    // ignore
                }
            }
        }
        return null;
    }

}
