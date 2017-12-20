package org.free.commons.components.log;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.PatternLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.support.PropertiesLoaderUtils;

/**
 * TODO
 * 
 * @author dongdaiming
 * @date 2017年12月20日
 */
public class Slf4jLoggerFactory {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Slf4jLoggerFactory.class);

	// key: name (String), value: a Log4jLoggerAdapter;
	private static final ConcurrentMap<String, Logger> loggerMap = new ConcurrentHashMap<String, Logger>();

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

	public static Logger getLoggerByOrg(String orgCode) {
		Logger slf4jLogger = loggerMap.get(orgCode);
		if (slf4jLogger != null) {
			return slf4jLogger;
		} else {
			org.apache.log4j.Logger log4jLogger;
			if (orgCode.equalsIgnoreCase(Logger.ROOT_LOGGER_NAME)) {
				log4jLogger = LogManager.getRootLogger();
			} else {
				log4jLogger = LogManager.getLogger(orgCode);
				FileAppender appender = new DailyRollingFileAppenderX();
				initFileAppender(orgCode, appender);
				ConsoleAppender cAppender = new ConsoleAppender();
				initConsoleAppender(orgCode, cAppender);
				initLogger(log4jLogger, appender, cAppender);
			}
			Logger newInstance = new Log4jLoggerAdapter(log4jLogger);
			Logger oldInstance = loggerMap.putIfAbsent(orgCode, newInstance);
			return oldInstance == null ? newInstance : oldInstance;
		}
	}
	
	public static Logger getLoggerByOrg(String name, String orgCode) {
		name = name + orgCode;
		Logger slf4jLogger = loggerMap.get(name);
		if (slf4jLogger != null) {
			return slf4jLogger;
		} else {
			org.apache.log4j.Logger log4jLogger;
			if (name.equalsIgnoreCase(Logger.ROOT_LOGGER_NAME)) {
				log4jLogger = LogManager.getRootLogger();
			} else {
				log4jLogger = LogManager.getLogger(name);
				FileAppender appender = new DailyRollingFileAppenderX();
				initFileAppender(orgCode, appender);
				ConsoleAppender cAppender = new ConsoleAppender();
				initConsoleAppender(orgCode, cAppender);
				initLogger(log4jLogger, appender, cAppender);
			}
			Logger newInstance = new Log4jLoggerAdapter(log4jLogger);
			Logger oldInstance = loggerMap.putIfAbsent(name, newInstance);
			return oldInstance == null ? newInstance : oldInstance;
		}
	}

	/**
	 * @param logger
	 * @param cAppender
	 * @param appender
	 */
	private static void initLogger(org.apache.log4j.Logger logger, FileAppender appender, ConsoleAppender cAppender) {
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

	private static Logger initLoggerByOrg(String orgCode) {
		Logger slf4jLogger = loggerMap.get(orgCode);
		if (slf4jLogger != null) {
			return slf4jLogger;
		} else {
			org.apache.log4j.Logger log4jLogger;
			if (orgCode.equalsIgnoreCase(Logger.ROOT_LOGGER_NAME)) {
				log4jLogger = LogManager.getRootLogger();
			} else {
				log4jLogger = LogManager.getLogger(orgCode);
				FileAppender appender = new DailyRollingFileAppenderX();
				initFileAppender(orgCode, appender);
				ConsoleAppender cAppender = new ConsoleAppender();
				initConsoleAppender(orgCode, cAppender);
				initLogger(log4jLogger, appender, cAppender);
			}
			Logger newInstance = new Log4jLoggerAdapter(log4jLogger);
			Logger oldInstance = loggerMap.putIfAbsent(orgCode, newInstance);
			return oldInstance == null ? newInstance : oldInstance;
		}
	}

}
