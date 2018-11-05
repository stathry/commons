/**
 * Copyright 2016-2100 free Co., Ltd.
 */
package org.stathry.commons.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * 
 * @author Demon@free.com
 *
 * 2016年8月5日
 */
public final class DatetimeUtils {

    public static final String DATETIME_ZONE_PATTERN1 = "yyyy-MM-dd'T'HH:mm:ss.SSSX";
    public static final String DATETIME_PATTERN1 = "yyyy-MM-dd HH:mm:ss";
    public static final String DATETIME_PATTERN2 = "yyyy/MM/dd HH:mm:ss";
    public static final String DATE_PATTERN1 = "yyyy-MM-dd";
    public static final String TIME_PATTERN1 = "HH:mm:ss";

    public static final String T00 = "00:00:00.000";
    public static final String T06= "08:00:00.000";

    private DatetimeUtils() {}
	
    public static String format(Date date) {
        return FastDateFormat.getInstance(DATETIME_PATTERN1, null, null).format(date);
    }

    public static String format(Date date, String pattern) {
        pattern = StringUtils.isBlank(pattern) ? DATETIME_PATTERN1 : pattern;
        return FastDateFormat.getInstance(pattern, null, null).format(date);
    }

    public static Date parseQuietly(String source, String pattern) {
        return parseQuietly(source, pattern, false);
    }

    public static Date parseQuietly(String source, String pattern, boolean lenient) {
        Date date = null;
        try {
            date = parse(source, pattern, lenient);
        } catch (ParseException e) {
            // ignore
        }
        return date;
    }

    public static Date parse(String source, String pattern) throws ParseException {
        return parse(source, pattern, false);
    }

    public static Date parse(String source, String pattern, boolean lenient) throws ParseException {
        if(StringUtils.isBlank(source)) {
            return null;
        }
        pattern = StringUtils.isBlank(pattern) ? DATETIME_PATTERN1 : pattern;

        SimpleDateFormat f = new SimpleDateFormat(pattern);
        f.setLenient(lenient);
        return f.parse(source);
    }

	/**
	 * 计算间隔年，按每年365天计算
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static long intervalOfYears(Date startTime, Date endTime) {
        return intervalOfDatetime(startTime, endTime, TimeUnit.DAYS) / 365;
	}
	
	/**
	 * 计算间隔月，按每月30天计算
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static long intervalOfMonths(Date startTime, Date endTime) {
	    return intervalOfDatetime(startTime, endTime, TimeUnit.DAYS) / 30;
	}
	
	/**
	 * intervalOfDatetime
	 * @param startTime
	 * @param endTime
	 * @param timeUnit
	 * @return
	 */
    public static long intervalOfDatetime(Date startTime, Date endTime, TimeUnit timeUnit) {
        if (startTime == null || endTime == null || timeUnit == null) {
            throw new NullPointerException();
        }

        long r, ms = endTime.getTime() - startTime.getTime();
        switch (timeUnit) {
            case DAYS:
                r = TimeUnit.MILLISECONDS.toDays(ms);
                break;
            case HOURS:
                r = TimeUnit.MILLISECONDS.toHours(ms);
                break;
            case MINUTES:
                r = TimeUnit.MILLISECONDS.toMinutes(ms);
                break;
            case SECONDS:
                r = TimeUnit.MILLISECONDS.toSeconds(ms);
                break;
            case MILLISECONDS:
                r = ms;
                break;
            case MICROSECONDS:
                r = TimeUnit.MILLISECONDS.toMicros(ms);
                break;
            case NANOSECONDS:
                r = TimeUnit.MILLISECONDS.toNanos(ms);
                break;
            default:
                throw new IllegalArgumentException("unknown timeUnit.");
        }
        return r;
    }

    public static Date nextClearDate(Date date, final int nextDateField, final int amount) {


        Date d;
        switch (nextDateField) {
            case Calendar.YEAR:
                d = DateUtils.truncate(date, Calendar.YEAR);
                d = DateUtils.addYears(d, amount);
                break;
            case Calendar.MONTH:
                d = DateUtils.truncate(date, Calendar.MONTH);
                d = DateUtils.addMonths(d, amount);
                break;
            case Calendar.DAY_OF_MONTH:
                d = DateUtils.truncate(date, Calendar.DAY_OF_MONTH);
                d = DateUtils.addDays(d, amount);
                break;
            case Calendar.HOUR_OF_DAY:
                d = DateUtils.truncate(date, Calendar.HOUR_OF_DAY);
                d = DateUtils.addHours(d, amount);
                break;
                case Calendar.MINUTE:
                d = DateUtils.truncate(date, Calendar.MINUTE);
                d = DateUtils.addMinutes(d, amount);
                break;
            case Calendar.SECOND:
                d = DateUtils.truncate(date, Calendar.SECOND);
                d = DateUtils.addSeconds(d, amount);
                break;
            default:
                throw new IllegalArgumentException("illegal nextDateField.");
        }
        return d;
    }

    public static int compareTime(Date date1, String time2, String pattern) {
        if(date1 == null || StringUtils.isBlank(time2) || StringUtils.isBlank(pattern)) {
            throw new IllegalArgumentException("required date1, time2, pattern");
        }
        String time1 = format(date1, pattern);
        return time1.compareTo(time2);
    }

    public static boolean isInTime(Date date, String time1, String time2, String pattern) {
        if(date == null || StringUtils.isBlank(time1) || StringUtils.isBlank(time2) || StringUtils.isBlank(pattern)) {
            throw new IllegalArgumentException("required date1, time2, pattern");
        }
        String time = format(date, pattern);
        return time.compareTo(time1) >= 0 && time.compareTo(time2) <= 0;
    }

    public static List<String> getMonthList(String pattern, int amount, boolean before) {
        if (StringUtils.isBlank(pattern)) {
            return Collections.emptyList();
        }
        List<String> list = new ArrayList<>(amount);
        Date now = DateUtils.truncate(new Date(), Calendar.MONTH);
        int n = before ? -1 : 1;
        for (int i = 0; i < amount; i++) {
            if (i != 0) {
                now = DateUtils.addMonths(now, n);
            }
            list.add(DateFormatUtils.format(now, pattern));
        }
        return list;
    }

}
