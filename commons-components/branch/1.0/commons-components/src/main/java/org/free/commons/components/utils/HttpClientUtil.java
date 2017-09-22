package org.free.commons.components.utils;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientUtil {

	private static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

	/**
	 * 
	 * @param uri
	 * @param requestBody
	 * @param timeout
	 *            millis
	 * @return
	 */
	public static String executePost(String uri, String requestBody, int timeout) {
		HttpPost httpPost = new HttpPost(uri);
		httpPost.addHeader(
				"User-Agent",
				"Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.102 Safari/537.36");
		httpPost.addHeader("Content-Type", "application/json");

		RequestConfig config = RequestConfig.custom().setSocketTimeout(timeout).build();
		httpPost.setConfig(config);
		CloseableHttpResponse response = null;
		try {
			HttpEntity entity = new StringEntity(requestBody, "utf-8");
			httpPost.setEntity(entity);
			response = HttpClientBuilder.create().build().execute(httpPost);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 200) {
				HttpEntity respEntity = response.getEntity();
				String str = EntityUtils.toString(respEntity, "utf-8");
				return str;
			}
		} catch (Exception e) {
			logger.error("uri=" + uri + "\t" + requestBody, e);
		} finally {
			if (response != null) {
				try {
					EntityUtils.consume(response.getEntity());
				} catch (Exception e) {
					logger.error("uri=" + uri + "\t" + requestBody, e);
				}
			}
		}
		return null;
	}

}
