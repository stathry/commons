package org.apache.http;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.junit.Assert;
import org.junit.Test;
import org.stathry.commons.http.HttpUtils;

import static org.junit.Assert.assertNotNull;

/**
 * HttpUtilsTest
 * Created by dongdaiming on 2018-06-05 11:30
 */
public class HttpUtilsTest {

    @Test
    public void testCreateHttpClient() {
        HttpClient client = HttpUtils.getHttpClient();
        System.out.println(client);
        Assert.assertNotNull(client);
        Assert.assertEquals(client, HttpUtils.getHttpClient());
    }

    @Test
    public void testPostJSONStr() {
        String s = HttpUtils.postJSONString("http://118.178.138.170/msg/HttpBatchSendSM", "hello");
        System.out.println(s);
    }

}
