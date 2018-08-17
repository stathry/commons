/**
 * Copyright 2016-2100 free Co., Ltd.
 */
package org.stathry.commons.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Demon@free.com
 *
 * 2016年8月5日
 */
public class DatetimeFormatUtils {
	
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
    /** yyyy-MM-dd'T'HH:mm:ss.SSSX */
	public static final String PATTERN_DATETIME8 = "yyyy-MM-dd'T'HH:mm:ss.SSSX";
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

	private static final boolean LENIENT = false;

    public static String format() {
        return format(new Date(), PATTERN_DATETIME);
    }

	public static String format(Date date) {
		return format(date, PATTERN_DATETIME);
	}

	public static String format(Date date, String pattern) {
		if (date == null || StringUtils.isBlank(pattern)) {
			return "";
		}

		return new SimpleDateFormat(pattern).format(date);
	}


	public static Date parse(String source) throws ParseException {
		return parse(source, PATTERN_DATE);
	}
	
	public static Date parse(String source, String pattern) throws ParseException {
		return parse(source, pattern, LENIENT);
	}
	
	public static Date parse(String source, String pattern, boolean lenient) throws ParseException {
		if(StringUtils.isBlank(source) || StringUtils.isBlank(pattern)) {
			return null;
		}

		SimpleDateFormat f = new SimpleDateFormat(PATTERN_DATETIME);
		f.setLenient(lenient);
		return f.parse(source);
	}

    public static Date parseQuietly(String source) {
        return parseQuietly(source, PATTERN_DATE);
    }

    public static Date parseQuietly(String source, String pattern) {
        return parseQuietly(source, pattern, LENIENT);
    }

    public static Date parseQuietly(String source, String pattern, boolean lenient) {
        if(StringUtils.isBlank(source) || StringUtils.isBlank(pattern)) {
            return null;
        }

        SimpleDateFormat f = new SimpleDateFormat(PATTERN_DATETIME);
        f.setLenient(lenient);

        Date date = null;
        try {
            date = f.parse(source);
        } catch (ParseException e) {
            // ignore
        }
        return date;
    }
	
}
