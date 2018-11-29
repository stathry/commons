package org.stathry.commons.http;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
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
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stathry.commons.utils.ApplicationContextUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * HttpUtils
 * Created by dongdaiming on 2018-11-22 18:36
 */
public class HttpUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpUtils.class);

    private static PoolingHttpClientBuilder poolingHttpClientBuilder;

    private static final String DEFAULT_CHARSETS = "utf-8";
    private static final Charset DEFAULT_CHARSET = Charset.forName("utf-8");
    private static final String COMMON_USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36;rokey.me/0.1";
    private static final int DEFAULT_SOCKET_TIMEOUT = 10000;

    public static HttpClient getHttpClient() {
        return getPoolingHttpClientBuilder().getHttpClient();
    }

    /**
     * postJSONString
     *
     * @param uri
     * @param requestBody
     * @return
     */
    public static String postJSONString(String uri, String requestBody) {
        return postJSONString(uri, requestBody, DEFAULT_SOCKET_TIMEOUT);
    }

    /**
     * postJSONString
     *
     * @param uri
     * @param requestBody
     * @param timeout(millis)
     * @return
     */
    public static String postJSONString(String uri, String requestBody, int timeout) {
        LOGGER.debug("invoke uri, params {}.", uri, requestBody);
        HttpPost httpPost = new HttpPost(uri);
        httpPost.addHeader("User-Agent", COMMON_USER_AGENT);
        httpPost.addHeader("Content-Type", ContentType.APPLICATION_JSON.getMimeType());

        httpPost.setConfig(RequestConfig.custom().setSocketTimeout(timeout).build());
        HttpResponse response = null;
        try {
            HttpEntity entity = new StringEntity(requestBody, DEFAULT_CHARSETS);
            httpPost.setEntity(entity);
            response = getHttpClient().execute(httpPost);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            LOGGER.info("invoke uri {}, response status {}, phrase {}.", uri, statusCode, statusLine.getReasonPhrase());
            if (statusCode == HttpStatus.SC_OK) {
                HttpEntity respEntity = response.getEntity();
                String str = EntityUtils.toString(respEntity, DEFAULT_CHARSETS);
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
            httpPost.releaseConnection();
        }
        return null;
    }

    /**
     * postJSONString
     *
     * @param uri
     * @param requestBody
     * @return
     */
    public static String postJSONString2(String uri, String requestBody) {
        LOGGER.debug("invoke uri, params {}.", uri, requestBody);
        HttpPost httpPost = new HttpPost(uri);
        httpPost.addHeader("User-Agent", COMMON_USER_AGENT);
        httpPost.addHeader("Content-Type", ContentType.APPLICATION_JSON.getMimeType());

        httpPost.setConfig(RequestConfig.custom().setSocketTimeout(10000).build());
        HttpResponse response = null;
        try {
            HttpEntity entity = new StringEntity(requestBody, DEFAULT_CHARSETS);
            httpPost.setEntity(entity);
            response = HttpClients.createDefault().execute(httpPost);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            LOGGER.debug("invoke uri {}, response status {}, phrase {}.", uri, statusCode, statusLine.getReasonPhrase());
            if (statusCode == HttpStatus.SC_OK) {
                HttpEntity respEntity = response.getEntity();
                String str = EntityUtils.toString(respEntity, DEFAULT_CHARSETS);
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
            httpPost.releaseConnection();
        }
        return null;
    }

    public static PoolingHttpClientBuilder getPoolingHttpClientBuilder() {
        if (poolingHttpClientBuilder == null) {
            synchronized (HttpUtils.class) {
                if (poolingHttpClientBuilder == null) {
                    try {
                        poolingHttpClientBuilder = ApplicationContextUtils.getBean(PoolingHttpClientBuilder.class);
                    } catch (Exception e) {
                    }
                }
                if (poolingHttpClientBuilder == null) {
                    poolingHttpClientBuilder = new PoolingHttpClientBuilder();
                }
            }
        }
        return poolingHttpClientBuilder;
    }

    public static String md5(String s) {
        return DigestUtils.md5Hex(s);
    }

    public static String base64Encode(String s) {
        return Base64.encodeBase64String(s.getBytes(DEFAULT_CHARSET));
    }

    public static String base64Decode(String s) {
        return new String(Base64.decodeBase64(s), DEFAULT_CHARSET);
    }


    public static String urlFormat(final List<? extends NameValuePair> parameters) {
        return URLEncodedUtils.format(parameters, '&', DEFAULT_CHARSETS);
    }

    public static String urlFormat(final List<? extends NameValuePair> parameters, final String charset) {
        return URLEncodedUtils.format(parameters, '&', charset);
    }

    public static String urlEncode(String str) {
        return urlEncode(str, DEFAULT_CHARSETS);
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

    public static List<NameValuePair> urlParse(String s) {
        return URLEncodedUtils.parse(s, DEFAULT_CHARSET);
    }

    public static List<NameValuePair> urlParse(String s, Charset charset) {
        return URLEncodedUtils.parse(s, charset);
    }

    public static String urlDecode(String str) {
        return urlDecode(str, DEFAULT_CHARSETS);
    }

    public static String urlDecode(String str, String enc) {
        String ds = "";
        try {
            ds = URLDecoder.decode(str, enc);
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("unsupported encoding " + enc, e);
        }
        return ds;
    }

    private static String concatParams(Map<String, ?> params) {
        StringBuilder b = new StringBuilder();
        String vs;
        for (Map.Entry<String, ?> e : params.entrySet()) {
            vs = e.getValue() == null ? null : e.getValue().toString();
            if (vs != null && vs.length() > 0) {
                b.append(e.getKey()).append('=').append(vs).append('&');
            }
        }
        b.deleteCharAt(b.length() - 1);
        return b.toString();
    }

    public static List<NameValuePair> mapToNameValues(Map<String, ?> params) {
        List<NameValuePair> list = new ArrayList<>(params.size());
        for (Map.Entry<String, ?> e : params.entrySet()) {
            list.add(new BasicNameValuePair(e.getKey(), e.getValue().toString()));
        }
        return list;
    }

}
