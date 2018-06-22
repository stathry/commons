package org.stathry.commons.log;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.stathry.commons.utils.ConfigManager;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import ch.qos.logback.core.util.StatusPrinter;

public class LogbackUtils {

	private static final LogConfig CONF = ConfigManager.get("app.logback", LogConfig.class);
	private static final String MSG_PATTERN = ConfigManager.get("app.logback.msgPattern");
	private static final String FILENAME_PATTERN = ConfigManager.get("app.logback.filenamePattern");
	private static final String FSEP = File.separator;
	private static final Map<String, OrgAppenders<ILoggingEvent>> orgAppenderMap = initOrgAppenders();
	@SuppressWarnings("unused")
	private static final LoggerContext rootContext = initRootContext();
	private static final Map<String, Logger> orgLoggers = new ConcurrentHashMap<>();
	
	private static LoggerContext initRootContext() {
		LoggerContext rootContext = (LoggerContext) LoggerFactory.getILoggerFactory();
		rootContext.reset();
		initRootAppender(rootContext);
		StatusPrinter.print(rootContext);
		return rootContext;
	}
	
	/**
	 * 
	 * @param context
	 * @param appenderName
	 * @param filename
	 * @param filenamePattern
	 * @param maxHis
	 * @return
	 */
	private static RollingFileAppender<ILoggingEvent> createRollFileAppender(LoggerContext context, String appenderName,
			String filename, String filenamePattern, int maxHis) {
		RollingFileAppender<ILoggingEvent> rappender = new RollingFileAppender<ILoggingEvent>();
		rappender.setName(appenderName);
		rappender.setContext(context);
		rappender.setFile(filename);
		TimeBasedRollingPolicy<ILoggingEvent> rpolicy = new TimeBasedRollingPolicy<>();
		rpolicy.setFileNamePattern(filenamePattern);
		rpolicy.setMaxHistory(maxHis);
		rpolicy.setContext(context);
		rpolicy.setParent(rappender);
		rpolicy.start();

		PatternLayoutEncoder encoder = new PatternLayoutEncoder();
		encoder.setContext(context);
		encoder.setPattern(MSG_PATTERN);
		encoder.start();

		rappender.setTriggeringPolicy(rpolicy);
		rappender.setRollingPolicy(rpolicy);
		rappender.setEncoder(encoder);

		rappender.start();
		return rappender;
	}

    /**
     *
     * @param context
     * @param appenderName
     * @return
     */
	private static ConsoleAppender<ILoggingEvent> createConsoleAppender(LoggerContext context, String appenderName) {
		ConsoleAppender<ILoggingEvent> rappender = new ConsoleAppender<ILoggingEvent>();
		rappender.setName(appenderName);
		rappender.setContext(context);

		PatternLayoutEncoder encoder = new PatternLayoutEncoder();
		encoder.setContext(context);
		encoder.setPattern(MSG_PATTERN);
		encoder.start();

		rappender.setEncoder(encoder);

		rappender.start();
		return rappender;
	}

	public static Logger getLogger(Class<?> clazz, String orgCode) {
		return getLogger0(clazz.getName(), orgCode);
	}
	
	public static Logger getLogger(String className, String orgCode) {
		return getLogger0(className, orgCode);
	}
	
	private static Logger getLogger0(String className, String orgCode) {
		if (!CONF.getOrgCodes().contains(StringUtils.upperCase(orgCode))) {
			throw new IllegalArgumentException("illegal log argument orgCode " + orgCode);
		}
		String logName = orgCode + "-" + className;
		Logger logger = orgLoggers.get(logName);
		if(logger != null) {
			return logger;
		}
		OrgAppenders<ILoggingEvent> orgApp = orgAppenderMap.get(orgCode);
		logger = orgApp.getContext().getLogger(orgCode + "-" + className);
		logger.setAdditive(false);
		logger.setLevel(Level.toLevel(CONF.getOrgLevel()));
		 logger.detachAndStopAllAppenders();
		logger.addAppender(orgApp.getFileAppender());
		logger.addAppender(orgApp.getConsoleAppender());
		orgLoggers.put(logName, logger);
		return logger;
	}

    /**
     *
     * @return
     */
	private static Map<String, OrgAppenders<ILoggingEvent>> initOrgAppenders() {
		Map<String, OrgAppenders<ILoggingEvent>> apps = new HashMap<>(CONF.getOrgCodes().size());
		RollingFileAppender<ILoggingEvent> fileApp;
		ConsoleAppender<ILoggingEvent> consoleApp;
		String fileAppName;
		String consoleAppName;
		String fileName;
		String fileNamePattern;
		for (String org : CONF.getOrgCodes()) {
			fileAppName = "ORG-" + org + "-RAppender";
			consoleAppName = "ORG-" + org + "-CAppender";
			fileName = CONF.getLogDir() + FSEP + org + CONF.getFilenameSuffix();
			fileNamePattern = CONF.getLogDir() + FSEP + org + FILENAME_PATTERN + CONF.getFilenameSuffix();
			
			LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory(); 
			fileApp = createRollFileAppender(context, fileAppName, fileName, fileNamePattern, CONF.getOrgMaxHistory());
			consoleApp = createConsoleAppender(context, consoleAppName);
			apps.put(org, new OrgAppenders<>(fileApp, consoleApp, context));
		}
		return apps;
	}

	/**
	 * @param context
	 */
	private static void initRootAppender(LoggerContext context) {
		String name = CONF.getLogDir() + FSEP + CONF.getFilenamePrefix() + CONF.getFilenameSuffix();
		String pattern = CONF.getLogDir() + FSEP + CONF.getFilenamePrefix() + FILENAME_PATTERN
				+ CONF.getFilenameSuffix();
		RollingFileAppender<ILoggingEvent> rappender = createRollFileAppender(context, "ROOT-RAppender", name, pattern,
				CONF.getMainMaxHistory());
		Logger ROOT = context.getLoggerList().get(0);
		ROOT.setAdditive(false);
		ROOT.addAppender(rappender);
		ConsoleAppender<ILoggingEvent> cappender = createConsoleAppender(context, "ROOT-CAppender");
		ROOT.addAppender(cappender);
		ROOT.setLevel(Level.toLevel(CONF.getMainLevel()));
	}
	
	protected static class OrgAppenders<T> {
		RollingFileAppender<T> fileAppender;
		ConsoleAppender<T> consoleAppender;
		LoggerContext context;

		public OrgAppenders(RollingFileAppender<T> fileAppender, ConsoleAppender<T> consoleAppender, LoggerContext context) {
			this.fileAppender = fileAppender;
			this.consoleAppender = consoleAppender;
			this.context = context;
		}

		public LoggerContext getContext() {
			return context;
		}

		public void setContext(LoggerContext context) {
			this.context = context;
		}

		public RollingFileAppender<T> getFileAppender() {
			return fileAppender;
		}

		public void setFileAppender(RollingFileAppender<T> fileAppender) {
			this.fileAppender = fileAppender;
		}

		public ConsoleAppender<T> getConsoleAppender() {
			return consoleAppender;
		}

		public void setConsoleAppender(ConsoleAppender<T> consoleAppender) {
			this.consoleAppender = consoleAppender;
		}

	}
}
