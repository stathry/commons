package org.stathry.commons.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientUtil {

	private static Logger LOGGER = LoggerFactory.getLogger(HttpClientUtil.class);
	
	private static PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
	
	static {
	    // 将连接池最大连接数设置到200
	     connectionManager.setMaxTotal(200);
	     // 将每个路由最大连接数设置为20
	     connectionManager.setDefaultMaxPerRoute(20);
	     
	     //将目标主机的最大连接数增加到50
	     connectionManager.setMaxPerRoute(new HttpRoute(new HttpHost("www.baidu.com", 80)), 30);
	}
	
	public static CloseableHttpClient getClientFromPool() {
	    return HttpClients.custom().setConnectionManager(connectionManager).build();
	}

	/**
	 * 
	 * @param uri
	 * @param requestBody
	 * @param timeout
	 *            millis
	 * @return
	 */
	public static String post(String uri, String requestBody, int timeout) {
	    LOGGER.debug("invoke uri {} with params {}.", uri, requestBody);
		HttpPost httpPost = new HttpPost(uri);
		httpPost.addHeader(
				"User-Agent",
				"Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.102 Safari/537.36");
		httpPost.addHeader("Content-Type", ContentType.APPLICATION_JSON.getMimeType());

		RequestConfig config = RequestConfig.custom().setSocketTimeout(timeout).build();
		httpPost.setConfig(config);
		CloseableHttpResponse response = null;
		try {
			HttpEntity entity = new StringEntity(requestBody, "utf-8");
			httpPost.setEntity(entity);
			CloseableHttpClient client = HttpClients.createDefault();
			System.out.println(client);
			response = client.execute(httpPost);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            LOGGER.info("invoke uri {} response status {}, reason {}.", uri, statusCode, statusLine.getReasonPhrase());
			if (statusCode == HttpStatus.SC_OK) {
				HttpEntity respEntity = response.getEntity();
				String str = EntityUtils.toString(respEntity, "utf-8");
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
	
	public static void main(String[] args) {
	    String s = post("http://apidata.credittone.com:51666/api/query", "{}", 3000);
	    System.out.println(s);
	    for(int i = 0; i < 300; i++) {
	        post("http://apidata.credittone.com:51666/api/query", "{}", 3000);
	    }
	}

}
