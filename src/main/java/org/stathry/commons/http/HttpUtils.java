package org.stathry.commons.http;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * HttpUtils
 * Created by dongdaiming on 2018-11-22 18:36
 */
public class HttpUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpUtils.class);

    private static final String DEFAULT_CHARSETS = "utf-8";
    private static final Charset DEFAULT_CHARSET = Charset.forName("utf-8");
    private static final String COMMON_USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36;rokey.me/0.1";
    private static final int DEFAULT_SOCKET_TIMEOUT = 10000;
    private static final int DEFAULT_KEEP_ALIVE_TIME = 5000;
    //    连接池最大连接数
    private static final int MAX_CONN_TOTAL = 1024;
    //    每个路由最大连接数
    private static final int MAX_CONN_PER_ROUTE = 8;
    //    连接超时时间
    private static final int CONNECTION_TIMEOUT = 5000;
    //    等待数据超时时间
    private static final int SOCKET_TIMEOUT = 10000;

    private static PoolingHttpClientConnectionManager pool;
    private static HttpClient httpClient;
    private static RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(SOCKET_TIMEOUT).build();

    public static String postJSONString(String uri, String requestBody) {
        return postJSONString(uri, requestBody, null, DEFAULT_SOCKET_TIMEOUT, DEFAULT_CHARSETS);
    }

    public static String postJSONString(String uri, String requestBody, Map<String, String> headerMap) {
        return postJSONString(uri, requestBody, headerMap, DEFAULT_SOCKET_TIMEOUT, DEFAULT_CHARSETS);
    }

    /**
     * postJSONString
     * @param uri
     * @param requestBody
     * @param headerMap
     * @param timeout
     * @param charset
     * @return
     */
    public static String postJSONString(String uri, String requestBody, Map<String, String> headerMap, int timeout, String charset) {
        LOGGER.debug("invoke uri, params {}.", uri, requestBody);
        HttpPost httpPost = new HttpPost(uri);
        httpPost.addHeader("User-Agent", COMMON_USER_AGENT);
        httpPost.addHeader("Content-Type", ContentType.APPLICATION_JSON.getMimeType());
        if (headerMap != null && !headerMap.isEmpty()) {
            for (Map.Entry<String, String> e : headerMap.entrySet()) {
                httpPost.addHeader(e.getKey(), e.getValue());
            }
        }

        httpPost.setConfig(timeout == SOCKET_TIMEOUT ? requestConfig : RequestConfig.custom().setSocketTimeout(timeout).build());
        HttpResponse response = null;
        HttpEntity entity = new StringEntity(requestBody, charset);
        httpPost.setEntity(entity);
        try {
            response = getHttpClient().execute(httpPost);
            StatusLine statusLine = response.getStatusLine();
            String responseBody = "";
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity respEntity = response.getEntity();
                responseBody = EntityUtils.toString(respEntity, DEFAULT_CHARSETS);
            }
            LOGGER.info("invoke uri {}, responseStatus {}, responsePhrase {}, responseBody: {}", uri, statusLine.getStatusCode(),
                    statusLine.getReasonPhrase(), StringUtils.abbreviate(responseBody, 200));

            return responseBody;
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

    public static String md5(String s) {
        return DigestUtils.md5Hex(s);
    }

    public static String encodeBase64(String s) {
        return Base64.encodeBase64String(s.getBytes(DEFAULT_CHARSET));
    }

    public static String decodeBase64(String s) {
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

    public static HttpClient getHttpClient() {
        if (httpClient == null) {
            synchronized (HttpUtils.class) {
                if (httpClient == null) {
                    if (pool == null) {
                        try {
                            pool = initPool();
                        } catch (Exception e) {
                            LOGGER.error(e.getMessage(), e);
                        }
                    }

                    httpClient = HttpClients.custom().setConnectionManager(pool).setDefaultRequestConfig(
                            RequestConfig.custom().setConnectTimeout(CONNECTION_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).build())
                            .setKeepAliveStrategy(new SimpleConnectionKeepAliveStrategy()).build();
                }
            }
        }
        return httpClient;
    }

    private static PoolingHttpClientConnectionManager initPool() throws Exception {
        RegistryBuilder<ConnectionSocketFactory> schemeRegistry = RegistryBuilder.create();
        schemeRegistry.register("http", PlainConnectionSocketFactory.getSocketFactory());

        SSLContext sslcontext = SSLContext.getInstance("TLS");
        sslcontext.init(new KeyManager[0], new TrustManager[]{new SimpleTrustManager()}, new SecureRandom());
        SSLConnectionSocketFactory sf = new SSLConnectionSocketFactory(sslcontext);
        schemeRegistry.register("https", sf);

        pool = new PoolingHttpClientConnectionManager(schemeRegistry.build());
        pool.setMaxTotal(MAX_CONN_TOTAL);
        pool.setDefaultMaxPerRoute(MAX_CONN_PER_ROUTE);
        pool.setDefaultSocketConfig(SocketConfig.custom().setSoTimeout(SOCKET_TIMEOUT).build());
        LOGGER.info("http connection pool is created, MAX_CONN_TOTAL {}, MAX_CONN_PER_ROUTE {}, SOCKET_TIMEOUT {} ms, CONNECTION_TIMEOUT {} ms.",
                MAX_CONN_TOTAL, MAX_CONN_PER_ROUTE, SOCKET_TIMEOUT, CONNECTION_TIMEOUT);
        return pool;
    }

    private static class SimpleTrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    private static class SimpleConnectionKeepAliveStrategy implements ConnectionKeepAliveStrategy {

        @Override
        public long getKeepAliveDuration(HttpResponse httpResponse, HttpContext httpContext) {
            Args.notNull(httpResponse, "HTTP response");
            final HeaderElementIterator it = new BasicHeaderElementIterator(httpResponse.headerIterator(HTTP.CONN_KEEP_ALIVE));
            while (it.hasNext()) {
                final HeaderElement he = it.nextElement();
                final String param = he.getName();
                final String value = he.getValue();
                if (value != null && param.equalsIgnoreCase("timeout")) {
                    try {
                        return Long.parseLong(value) * 1000;
                    } catch (final NumberFormatException ignore) {
                    }
                }
            }
            return DEFAULT_KEEP_ALIVE_TIME;
        }
    }

}
