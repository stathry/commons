/**
 * Copyright 2016-2100 free Co., Ltd.
 */
package org.free.commons.components.utils;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

/**
 * 
 * @author Demon@free.com
 *
 * 2016年8月5日
 */
public final class DatetimeUtils extends DateUtils {
	
	public static void main(String[] args) throws ParseException {
		System.out.println(intervalOfMonths(parseDate("20161114", "yyyyMMdd"), parseDate("20161114", "yyyyMMdd")));
//		System.out.println(addMonths(parseDate("20170413", "yyyyMMdd"), 29).toLocaleString());
	}
	
	/**
	 * 计算间隔年，按每年365天计算
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static long intervalOfYears(Date startTime, Date endTime) {
		long days = 0;
		if (endTime == null) {
			return days;
		}

		if (startTime == null) {
			startTime = new Date();
		}
		
		days = intervalOfDays(startTime, endTime);
		return days/365;
	}
	
	/**
	 * 计算间隔月，按每月30天计算
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static long intervalOfMonths(Date startTime, Date endTime) {
		long days = 0;
		if (endTime == null) {
			return days;
		}
		
		if (startTime == null) {
			startTime = new Date();
		}
		
		days = intervalOfDays(startTime, endTime);
		return days/30;
	}
	
	/**
	 * 计算间隔日期
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static long intervalOfDays(Date startTime, Date endTime) {
		int days = 0;
		if (endTime == null) {
			return days;
		}
		
		if (startTime == null) {
			startTime = new Date();
		}
		
		Date endTime1 = DateUtils.truncate(endTime, Calendar.DAY_OF_MONTH);
		Date startTime1 = DateUtils.truncate(startTime, Calendar.DAY_OF_MONTH);
		long millis = endTime1.getTime() - startTime1.getTime();
		return TimeUnit.MILLISECONDS.toDays(millis);
	}
	
	
	/**
	 * 计算间隔日期
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws ParseException 
	 */
	public static long intervalOfDays(String start, String end, String pattern) throws ParseException {
		long days = 0;
		Date startTime = null;
		Date endTime = null;

		if (StringUtils.isBlank(end)) {
			return days;
		}

		startTime = StringUtils.isBlank(start) ? new Date() : DatetimeFormatUtils.parseDatetime(start, pattern);
		endTime = DatetimeFormatUtils.parseDatetime(end, pattern);

		if (startTime == null || endTime == null) {
			return 0;
		}
		long millis = endTime.getTime() - startTime.getTime();
		return TimeUnit.MILLISECONDS.toDays(millis);
	}
	
	/**
	 * 计算间隔小时
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static long intervalOfHours(Date startTime, Date endTime) {
		long millis = 0;
		
		if (endTime == null) {
			return 0;
		}
		
		if(startTime == null) {
			startTime = new Date();
		}
		
		millis = intervalOfMillis(startTime, endTime);
		return TimeUnit.MILLISECONDS.toHours(millis);
	}
	
	/**
	 * 计算间隔分钟
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static long intervalOfMinutes(Date startTime, Date endTime) {
		if (endTime == null) {
			return 0;
		}
		
		if (startTime == null) {
			startTime = new Date();
		}
		
		long millis = intervalOfMillis(startTime, endTime);
		return TimeUnit.MILLISECONDS.toMinutes(millis);
	}
	
	/**
	 * 计算间隔秒
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static long intervalOfSeconds(Date startTime, Date endTime) {
		if (endTime == null) {
			return 0;
		}
		
		if (startTime == null) {
			startTime = new Date();
		}
		
		long millis = intervalOfMillis(startTime, endTime);
		return TimeUnit.MILLISECONDS.toSeconds(millis);
	}
	
	/**
	 * 计算间隔毫秒
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static long intervalOfMillis(Date startTime, Date endTime) {
		long millis = 0;
		if (endTime == null) {
			return millis;
		}

		if (startTime == null) {
			startTime = new Date();
		}
		
		millis = endTime.getTime() - startTime.getTime();
		return millis;
	}
	
}
