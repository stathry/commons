package org.apache.http;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Assert;
import org.junit.Test;
import org.stathry.commons.http.HttpUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;

/**
 * HttpUtilsTest
 * Created by dongdaiming on 2018-06-05 11:30
 */
public class HttpUtilsTest {

    @Test
    public void testGetSingleHttpClient() {
        HttpClient client = HttpUtils.getHttpClient();
        System.out.println(client);
        Assert.assertNotNull(client);
        Assert.assertEquals(client, HttpUtils.getHttpClient());
    }

    @Test
    public void testGetDefaultHttpClient() {
        HttpClient client1 = HttpClients.createDefault();
        HttpClient client2 = HttpClients.createDefault();
        Assert.assertNotNull(client1);
        Assert.assertNotNull(client2);
        Assert.assertNotEquals(client1, client2);
    }

    @Test
    public void testCreateSystemHttpClient() {
        HttpClient client1 = HttpClients.createSystem();
        HttpClient client2 = HttpClients.createSystem();
        assertNotNull(client1);
        assertNotNull(client2);
        Assert.assertNotEquals(client1, client2);
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

    @Test
    public void testPostJSONStr() {
        String s = HttpUtils.postJSONString("http://118.178.138.170/msg/HttpBatchSendSM", "hello");
        System.out.println(s);
        Assert.assertTrue(StringUtils.isNotBlank(s));

        String s2 = HttpUtils.postJSONString("https://test.xinyan.com/product/integrity/v1/loans", "hello");
        System.out.println(s2);
        Assert.assertTrue(StringUtils.isNotBlank(s2));
    }

    @Test
    public void testUrlEncode() {
        Assert.assertEquals("abc123", HttpUtils.urlEncode("abc123"));

        String s = "$xyz666中文abc123";
        String es = HttpUtils.urlEncode(s);
        System.out.println(es);
        String des = HttpUtils.urlDecode(es, "utf-8");
        System.out.println(des);
        Assert.assertEquals(s, des);

        List<NameValuePair> list = new ArrayList<>();
        list.add(new BasicNameValuePair("k1", "v1"));
        list.add(new BasicNameValuePair("k2", "值2"));
        list.add(new BasicNameValuePair("k3", "666.88"));
        System.out.println(list);
        String sl = HttpUtils.urlFormat(list);
        System.out.println(sl);
        List<NameValuePair> sl2 = HttpUtils.urlParse(sl);
        System.out.println(sl2);
        Assert.assertEquals(sl2, list);
    }

    @Test
    public void testDigest() {
        String s1 = HttpUtils.md5("hello");
        String s2 = HttpUtils.md5("禁忌");
        System.out.println(s1);
        System.out.println(s2);
        Assert.assertTrue(s1.length() == s2.length() && s1.length() == 32);

        String s30 = "hello";
        String s40 = "禁忌";
        String s3 = HttpUtils.encodeBase64(s30);
        String s4 = HttpUtils.encodeBase64(s40);
        System.out.println(s3);
        System.out.println(s4);
        String s31 = HttpUtils.decodeBase64(s3);
        String s41 = HttpUtils.decodeBase64(s4);
        System.out.println(s31);
        System.out.println(s41);
        Assert.assertEquals(s31, s30);
        Assert.assertEquals(s41, s40);
    }

}
