package org.free.commons.components.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 第三方日志文件分类-适配slf4j
 * 
 * @author dongdaiming
 * @date 2017年12月20日
 */
public class Slf4jUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(Slf4jUtil.class);

	private Slf4jUtil() {
	}

	public static Logger getLogger(Class<?> clazz) {
		return LoggerFactory.getLogger(clazz);
	}

	public static Logger getLogger(String name) {
		return LoggerFactory.getLogger(name);
	}

	public static Logger getLoggerByOrg(String orgCode) {
		return Slf4jLoggerFactory.getLoggerByOrg(orgCode);
	}

	public static Logger getLoggerByOrg(Class<?> clazz, String orgCode) {
		return getLoggerByOrg(clazz.getName(), orgCode);
	}

	public static Logger getLoggerByOrg(String name, String orgCode) {
		return Slf4jLoggerFactory.getLoggerByOrg(name, orgCode);
	}
}
