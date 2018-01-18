package org.free.commons.components.utils;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * TODO
 * 
 * @author dongdaiming
 * @date 2018年1月18日
 */
public class ConfigManager {
	private static final String FILENAME = "config.properties";
	private static Configuration conf;

	static {
		try {
			conf = new PropertiesConfiguration(FILENAME);
		} catch (ConfigurationException e) {
			throw new IllegalStateException(FILENAME);
		}
	}

	private ConfigManager() {
	}

	public static Configuration getConfig() {
		return conf;
	}

	public static int[] getIntArray(String key) {
		String[] strArr = conf.getStringArray(key);
		int[] intArr = new int[strArr.length];
		for (int i = 0; i < strArr.length; i++) {
			intArr[i] = Integer.parseInt(strArr[i]);
		}
		return intArr;
	}

	public static int getInt(String key) {
		return conf.getInt(key);
	}
}
