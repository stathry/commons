/**
 * Copyright 2016-2100 Deppon Co., Ltd.
 */
package org.free.commons.components.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Demon@deppon.com
 *
 * 2016年8月5日
 */
public class DatetimeFormatUtils extends DateFormatUtils {
	
	private static Logger LOGGER = LoggerFactory.getLogger(DatetimeFormatUtils.class); 
	/** yyyy-MM-dd HH:mm:ss */
	public static final String PATTERN_DATETIME = "yyyy-MM-dd HH:mm:ss";
	/** yyyy_MM_dd HH:mm:ss */
	public static final String PATTERN_DATETIME1 = "yyyy_MM_dd HH:mm:ss";
	/** yyyy/MM/dd HH:mm:ss */
	public static final String PATTERN_DATETIME2 = "yyyy/MM/dd HH:mm:ss";
	/** yyyyMMddHHmmss */
	public static final String PATTERN_DATETIME3 = "yyyyMMddHHmmss";
	/** yyyyMMddHHmmssS */
	public static final String PATTERN_DATETIME4 = "yyyyMMddHHmmssS";
	/** yyyy-MM-dd hh:mm:ss a */
	public static final String PATTERN_DATETIME5 = "yyyy-MM-dd hh:mm:ss a";
	/** yyyy年M月d日  HH:mm:ss E */
	public static final String PATTERN_DATETIME6 = "yyyy年M月d日  HH:mm:ss E";
	/** yyyy-MM-dd HH:mm:ss E */
	public static final String PATTERN_DATETIME7 = "yyyy-MM-dd HH:mm:ss E";
	/** yyyy-MM-dd */
	public static final String PATTERN_DATE = "yyyy-MM-dd";
	/** yyyy_MM_dd */
	public static final String PATTERN_DATE1 = "yyyy_MM_dd";
	/** yyyy/MM/dd */
	public static final String PATTERN_DATE2 = "yyyy/MM/dd";
	/** yyyyMMdd */
	public static final String PATTERN_DATE3 = "yyyyMMdd";
	/** HH:mm:ss */
	public static final String PATTERN_TIME = "HH:mm:ss";
	/** HHmmss */
	public static final String PATTERN_TIME1 = "HHmmss";
	/** HH:mm:ss:S */
	public static final String PATTERN_TIME2 = "HH:mm:ss:S";
	/** HHmmssS */
	public static final String PATTERN_TIME3 = "HHmmssS";
	/** E */
	public static final String PATTERN_WEEK = "E";

	private static SimpleDateFormat SDF = null;
	private static final boolean LENIENT = false;
	
	static {
		SDF = new SimpleDateFormat(PATTERN_DATETIME);
		SDF.setLenient(LENIENT);
	}
	

	/**
	 * 格式化日期时间 默认格式'yyyy-MM-dd HH:mm:ss'
	 * @param date
	 * @return string
	 */
	public static String formatDatetime(Date date) {
		if (date == null) {
			return null;
		}
			SDF.applyPattern(PATTERN_DATETIME);
		return SDF.format(date);
	}

	/**
	 *格式化日期时间
	 * @param date
	 * @param pattern
	 * @return string
	 */
	public static String formatDatetime(Date date, String pattern) {
		if (StringUtils.isBlank(pattern)) {
			return formatDatetime(date);
		}
		
		try {
			SDF.applyPattern(pattern);
		} catch (Exception e) {
			LOGGER.warn("the given pattern is invalid", e);
			return null;
		}
		
		return SDF.format(date);
	}
	
	/**
	 * 格式化日期，默认格式'yyyy-MM-dd'
	 * @param date
	 * @return
	 */
	public static String formatDate(Date date) {
		if (date == null) {
			return null;
		}
		SDF.applyPattern(PATTERN_DATE);
		return SDF.format(date);
	}
	
	/**
	 * 格式化日期
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String formatDate(Date date, String pattern) {
		if (StringUtils.isBlank(pattern)) {
			return formatDate(date);
		}
		
		try {
			SDF.applyPattern(pattern);
		} catch (Exception e) {
			LOGGER.warn("the given pattern is invalid", e);
			return null;
		}
		
		return SDF.format(date);
	}
	
	/**
	 * 格式化时间，默认格式'HH:mm:ss'
	 * @param date
	 * @return string
	 */
	public static String formatTime(Date date) {
		if (date == null) {
			return null;
		}
		SDF.applyPattern(PATTERN_TIME);
		return SDF.format(date);
	}
	
	/**
	 * 格式化时间
	 * @param date
	 * @param pattern
	 * @return string
	 */
	public static String formatTime(Date date, String pattern) {
		if (StringUtils.isBlank(pattern)) {
			return formatTime(date);
		}
		
		try {
			SDF.applyPattern(pattern);
		} catch (Exception e) {
			LOGGER.warn("the given pattern is invalid", e);
			return null;
		}
		
		return SDF.format(date);
	}

	/**
	 * 解析日期时间(pattern:yyyy-MM-dd HH:mm:ss)
	 * @param source
	 * @return
	 * @throws ParseException
	 */
	public static Date parseDatetime(String source) throws ParseException {
		return parseDatetime(source, PATTERN_DATETIME);
	}

	/**
	 * 解析日期时间
	 * @param source
	 * @param pattern 默认yyyy-MM-dd HH:mm:ss
	 * @return
	 * @throws ParseException
	 */
	public static Date parseDatetime(String source, String pattern) throws ParseException {
		return parseDatetime(source, pattern, LENIENT);
	}
	
	/**
	 * 解析日期时间
	 * @param source
	 * @param pattern 默认yyyy-MM-dd HH:mm:ss 
	 * @param lenient
	 * @return
	 * @throws ParseException
	 */
	public static Date parseDatetime(String source, String pattern, boolean lenient) throws ParseException {
		if(StringUtils.isBlank(source)) {
			return null;
		}
		if(StringUtils.isBlank(pattern)) {
			SDF.applyPattern(PATTERN_DATETIME1);
		} else {
			SDF.applyPattern(pattern);
		}
		SDF.setLenient(lenient);
		return SDF.parse(source);
	}
	/**
	 * 解析日期(pattern:yyyy_MM_dd)
	 * @param source
	 * @return
	 * @throws ParseException
	 */
	public static Date parseDate(String source) throws ParseException {
		return parseDate(source, PATTERN_DATE);
	}
	
	/**
	 * 解析日期时间
	 * @param source
	 * @param pattern 默认yyyy-MM-dd
	 * @return
	 * @throws ParseException
	 */
	public static Date parseDate(String source, String pattern) throws ParseException {
		return parseDate(source, pattern, LENIENT);
	}
	
	/**
	 * 解析日期时间
	 * @param source
	 * @param pattern 默认yyyy-MM-dd
	 * @param lenient
	 * @return
	 * @throws ParseException
	 */
	public static Date parseDate(String source, String pattern, boolean lenient) throws ParseException {
		if(StringUtils.isBlank(source)) {
			return null;
		}
		if(StringUtils.isBlank(pattern)) {
			SDF.applyPattern(PATTERN_DATE);
		} else {
			SDF.applyPattern(pattern);
		}
		SDF.setLenient(lenient);
		return SDF.parse(source);
	}
	
}
