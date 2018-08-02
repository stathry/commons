package org.apache.http;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * TODO
 * Created by dongdaiming on 2018-06-05 11:30
 */
public class HttpClientsTest {

    @Test
    public void testCreateDefaultHttpClient() {
        CloseableHttpClient client = HttpClients.createDefault();
        System.out.println(client);
        assertNotNull(client);
    }

    @Test
    public void testCreateSystemHttpClient() {
        CloseableHttpClient client = HttpClients.createSystem();
        System.out.println(client);
        assertNotNull(client);
    }

    @Test
    public void testCreateCustomHttpClient() {
        PoolingHttpClientConnectionManager httpManager = new PoolingHttpClientConnectionManager();
        httpManager.setMaxTotal(300); // 连接池最大并发连接数
        httpManager.setDefaultMaxPerRoute(50); // 单路由最大并发数
        System.out.println("hp.getDefaultConnectionConfig:" + httpManager.getDefaultConnectionConfig());
        System.out.println("hp.getDefaultSocketConfig:" + httpManager.getDefaultSocketConfig());
        RequestConfig rc = RequestConfig.custom().setSocketTimeout(4000).setConnectTimeout(10000).setConnectionRequestTimeout(1000).build();
        System.out.println("rc:" + rc);

        HttpClientBuilder httpBuilder = HttpClients.custom().setConnectionManager(httpManager).setDefaultRequestConfig(rc);

        CloseableHttpClient client = httpBuilder.build();
        System.out.println(client);
        assertNotNull(client);
    }
}