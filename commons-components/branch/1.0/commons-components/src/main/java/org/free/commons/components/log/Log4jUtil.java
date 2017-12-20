package org.free.commons.components.log;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.springframework.core.io.support.PropertiesLoaderUtils;

/**
 * 第三方日志文件分类-基于log4j
 * 
 * @author dongdaiming
 * @date 2017年12月20日
 */
public class Log4jUtil {

	private static final Logger LOGGER = Logger.getLogger(Log4jUtil.class);

	private static final String KEY_ORGS = "log4j.orgCodes";
	private static final String KEY_LEVEL = "log4j.level";
	private static final String KEY_PATTERN = "log4j.conversionPattern";
	private static final String ENCODING = "UTF-8";
	private static final String PREFIX = "logs/";
	private static final String SURRFIX = ".log";
	private static final boolean APPEND = true;
	private static final boolean ADDITIVITY = false;
	private static final Level DEFAULT_LEVEL = Level.INFO;
	private static final String DEFAULT_PATTERN = "%d %p [%t] [%C#%L] - %m%n";

	private static final Set<String> ORGS;
	private static Level LEVEL;
	private static String CONVERSIONPATTERN;
	private static Map<String, Logger> ORG_LOGS;

	static {
		ORGS = new HashSet<>();
		Properties prop = null;
		try {
			prop = PropertiesLoaderUtils.loadAllProperties("config.properties");
		} catch (IOException e) {
			LOGGER.error("load config.properties error.", e);
		}
		if (prop != null) {
			LEVEL = Level.toLevel(prop.getProperty(KEY_LEVEL), DEFAULT_LEVEL);
			CONVERSIONPATTERN = prop.getProperty(KEY_PATTERN, DEFAULT_PATTERN);
			String corgs = prop.getProperty(KEY_ORGS);
			if (StringUtils.isNotBlank(corgs)) {
				String[] array = corgs.split(",");
				if (array != null && array.length > 0) {
					ORG_LOGS = new HashMap<>(array.length);
					for (String org : array) {
						ORGS.add(org.toUpperCase());
						ORG_LOGS.put(org, initLoggerByOrg(org));
					}
				}
			}
		}
	}

	private Log4jUtil() {
	}

	public static Logger getLogger(String name) {
		return Logger.getLogger(name);
	}

	public static Logger getLogger(Class<?> clazz) {
		return Logger.getLogger(clazz);
	}

	public static Logger getLogger(Class<?> clazz, String orgCode) {
		return getLogger(clazz.getName(), orgCode);
	}

	public static Logger getLogger(String name, String orgCode) {
		checkOrg(orgCode);
		Logger logger = Logger.getLogger(name + orgCode);
		FileAppender appender = new DailyRollingFileAppender();
		initFileAppender(orgCode, appender);
		ConsoleAppender cAppender = new ConsoleAppender();
		initConsoleAppender(orgCode, cAppender);
		initLogger(logger, appender, cAppender);
		return logger;
	}

	public static Logger getLoggerByOrg(String orgCode) {
		return ORG_LOGS.get(orgCode);
	}

	private static Logger initLoggerByOrg(String orgCode) {
		Logger logger = Logger.getLogger(orgCode);
		FileAppender appender = new DailyRollingFileAppender();
		initFileAppender(orgCode, appender);
		ConsoleAppender cAppender = new ConsoleAppender();
		initConsoleAppender(orgCode, cAppender);
		initLogger(logger, appender, cAppender);
		return logger;
	}

	/**
	 * @param orgCode
	 */
	private static void checkOrg(String orgCode) {
		if (!ORGS.contains(StringUtils.upperCase(orgCode))) {
			throw new IllegalArgumentException("illegal argument orgCode " + orgCode);
		}
	}

	/**
	 * @param logger
	 * @param cAppender
	 * @param appender
	 */
	private static void initLogger(Logger logger, FileAppender appender, ConsoleAppender cAppender) {
		logger.removeAllAppenders();
		logger.setLevel(LEVEL);
		logger.setAdditivity(ADDITIVITY);
		logger.addAppender(appender);
		logger.addAppender(cAppender);
	}

	/**
	 * @param orgCode
	 * @param cAppender
	 */
	private static void initConsoleAppender(String orgCode, ConsoleAppender cAppender) {
		PatternLayout layout = new PatternLayout();
		layout.setConversionPattern(CONVERSIONPATTERN);
		cAppender.setLayout(layout);
		cAppender.setEncoding(ENCODING);
		cAppender.activateOptions();

	}

	/**
	 * @param orgCode
	 * @param appender
	 */
	private static void initFileAppender(String orgCode, FileAppender appender) {
		PatternLayout layout = new PatternLayout();
		layout.setConversionPattern(CONVERSIONPATTERN);
		appender.setLayout(layout);
		appender.setFile(PREFIX + orgCode + SURRFIX);
		appender.setEncoding(ENCODING);
		appender.setAppend(APPEND);
		appender.activateOptions();
	}

}
