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
	
	/** yyyy-MM-dd HH:mm:ss */
	public static final String PATTERN_DATETIME = "yyyy-MM-dd HH:mm:ss";

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
		return parse(source, PATTERN_DATETIME);
	}
	
	public static Date parse(String source, String pattern) throws ParseException {
		return parse(source, pattern, LENIENT);
	}
	
	public static Date parse(String source, String pattern, boolean lenient) throws ParseException {
		if(StringUtils.isBlank(source) || StringUtils.isBlank(pattern)) {
			return null;
		}

		SimpleDateFormat f = new SimpleDateFormat(pattern);
		f.setLenient(lenient);
		return f.parse(source);
	}

    public static Date parseQuietly(String source) {
        return parseQuietly(source, PATTERN_DATETIME);
    }

    public static Date parseQuietly(String source, String pattern) {
        return parseQuietly(source, pattern, LENIENT);
    }

    public static Date parseQuietly(String source, String pattern, boolean lenient) {
        if(StringUtils.isBlank(source) || StringUtils.isBlank(pattern)) {
            return null;
        }

        SimpleDateFormat f = new SimpleDateFormat(pattern);
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
