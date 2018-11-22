package org.stathry.commons.http;

import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * http连接池创建辅助类
 */

@Component
public class PoolingHttpClientBuilder implements InitializingBean,DisposableBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(PoolingHttpClientBuilder.class);

    private static final SimpleConnectionKeepAliveStrategy DEFAULT_ALIVE_STRATEGY = new SimpleConnectionKeepAliveStrategy();

    private static final int DEFAULT_KEEP_ALIVE_TIME = 5000;
//    连接池最大连接数
    private int maxTotal = 128;
//    每个路由最大连接数
    private int maxPerRoute = 8;
//    连接超时时间
    private int connectionTimeout = 5000;
//    等待数据超时时间
    private int socketTimeout = 10000;

    private PoolingHttpClientConnectionManager pool = null;
    private HttpClient httpClient;

    public PoolingHttpClientBuilder() {
    }

    public PoolingHttpClientBuilder(int maxTotal, int maxPerRoute, int connectionTimeout, int socketTimeout) {
        this.maxTotal = maxTotal;
        this.maxPerRoute = maxPerRoute;
        this.connectionTimeout = connectionTimeout;
        this.socketTimeout = socketTimeout;
    }

    public HttpClient getHttpClient() {
        if (httpClient == null) {
            synchronized (this) {
                if (httpClient == null) {
                    if (pool == null) {
                        try {
                            afterPropertiesSet();
                        } catch (Exception e) {
                            LOGGER.error(e.getMessage(), e);
                        }
                    }

                    httpClient = HttpClients.custom().setConnectionManager(pool).setDefaultRequestConfig(
                            RequestConfig.custom().setConnectTimeout(connectionTimeout).setSocketTimeout(socketTimeout).build())
                            .setKeepAliveStrategy(DEFAULT_ALIVE_STRATEGY).build();
                }
            }
        }
        return httpClient;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        RegistryBuilder<ConnectionSocketFactory> schemeRegistry = RegistryBuilder.create();
        schemeRegistry.register("http", PlainConnectionSocketFactory.getSocketFactory());

        SSLContext sslcontext = SSLContext.getInstance("TLS");
        sslcontext.init(new KeyManager[0], new TrustManager[]{new SimpleTrustManager()}, new SecureRandom());
        SSLConnectionSocketFactory sf = new SSLConnectionSocketFactory(sslcontext);
        schemeRegistry.register("https", sf);

        pool = new PoolingHttpClientConnectionManager(schemeRegistry.build());
        pool.setMaxTotal(maxTotal);
        pool.setDefaultMaxPerRoute(maxPerRoute);
        pool.setDefaultSocketConfig(SocketConfig.custom().setSoTimeout(socketTimeout).build());
        LOGGER.info("http connection pool is created, maxTotal {}, maxPerRoute {}, socketTimeout {} ms, connectionTimeout {} ms.",
                maxTotal, maxPerRoute, socketTimeout, connectionTimeout);
    }

    @Override
    public void destroy() throws Exception {
        LOGGER.info("Http connection pool will be destroyed...");
        if (pool != null) {
            pool.shutdown();
        }
        LOGGER.info("Http connection pool destroyed!");
    }

    public int getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }

    public int getMaxPerRoute() {
        return maxPerRoute;
    }

    public void setMaxPerRoute(int maxPerRoute) {
        this.maxPerRoute = maxPerRoute;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
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
                    } catch(final NumberFormatException ignore) {
                    }
                }
            }
            return DEFAULT_KEEP_ALIVE_TIME;
        }
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
}
