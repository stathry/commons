package org.bryadong.commons.utils;

import org.apache.commons.configuration.AbstractConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import com.alibaba.fastjson.JSON;

/**
 * TODO
 * 
 * @author dongdaiming
 * @date 2018年1月18日
 */
public class ConfigManager {
	private static final String FILENAME = "config.properties";
	private static final Configuration CONF;

	static {
		AbstractConfiguration.setDefaultListDelimiter('~');
		try {
			CONF = new PropertiesConfiguration(FILENAME);
		} catch (ConfigurationException e) {
			throw new IllegalStateException(FILENAME);
		}
	}

	private ConfigManager() {
	}

	public static String get(String key) {
		return CONF.getString(key);
	}

	public static int getInt(String key) {
		return CONF.getInt(key);
	}

	public static int[] getIntArray(String key) {
		String[] strArr = CONF.getStringArray(key);
		int[] intArr = new int[strArr.length];
		for (int i = 0; i < strArr.length; i++) {
			intArr[i] = Integer.parseInt(strArr[i]);
		}
		return intArr;
	}
	
	public static String[] getStringArray(String key) {
		return CONF.getStringArray(key);
	}

	public static <T> T get(String key, Class<T> clazz) {
		return JSON.parseObject(CONF.getString(key), clazz);
	}

	public static Configuration getConfig() {
		return CONF;
	}
}
