/**
 * Copyright 2012-2016 free Co., Ltd.
 */
package org.stathry.commons.utils;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public final class DataFormatUtils {

    private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
    private static final String DEFAULT_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final int DEFAULT_FLOAT_SCALE = 2;
    private static final RoundingMode DEFAULT_FLOAT_MODE = RoundingMode.HALF_UP;

    public static final int TYPE_STR = 0;
    public static final int TYPE_INT = 1;
    public static final int TYPE_FLOAT = 2;
    public static final int TYPE_DATE = 3;

    private final DecimalUtils decimalUtils;
    private final SimpleDateFormat dateFormat;

    public DataFormatUtils() {
        decimalUtils = new DecimalUtils();
        this.dateFormat = new SimpleDateFormat(DEFAULT_DATETIME_PATTERN);
    }

    public DataFormatUtils(int precision, int scale, RoundingMode mode, String datePattern) {
        decimalUtils = new DecimalUtils(precision, scale, mode);
        this.dateFormat = new SimpleDateFormat(datePattern);
    }

    public static String format(Object data) {
        return format(data, TYPE_STR, DEFAULT_FLOAT_SCALE, DEFAULT_FLOAT_MODE, "", DEFAULT_DATETIME_PATTERN);
    }

    public static String format(Object data, int type) {
        return format(data, type, DEFAULT_FLOAT_SCALE, DEFAULT_FLOAT_MODE, "", DEFAULT_DATETIME_PATTERN);
    }

    public static String formatDate(Object data, String srcPattern, String destPattern) {
        return format(data, TYPE_DATE, DEFAULT_FLOAT_SCALE, DEFAULT_FLOAT_MODE, srcPattern, destPattern);
    }

    public static String formatFloat(Object data, int scale, RoundingMode roundingMode) {
        return format(data, TYPE_FLOAT, scale, roundingMode, "", "");
    }

    /**
     * 数据格式化
     *
     * @param data
     * @param type
     * @param scale
     * @param roundingMode
     * @param srcPattern
     * @param destPattern
     * @return
     */
    public static String format(Object data, int type, int scale, RoundingMode roundingMode, String srcPattern, String destPattern) {
        if (data == null) {
            return "";
        }

        String value;
        if (TYPE_STR == type && data instanceof CharSequence) {
            value = data.toString();
        } else if (TYPE_FLOAT == type || data instanceof Double || data instanceof Float || data instanceof BigDecimal) {
            value = DecimalUtils.format(data, scale, roundingMode);
        } else if (TYPE_INT == type || data instanceof Integer || data instanceof Long) {
            value = data.toString();
        } else if (data instanceof Date) {
            value = DateFormatUtils.format((Date) data, destPattern);
        } else if (data instanceof Calendar) {
            value = DateFormatUtils.format((Calendar) data, destPattern);
        } else if (data instanceof CharSequence && TYPE_DATE == type) {
            try {
                value = DateFormatUtils.format(DateUtils.parseDateStrictly(data.toString(), srcPattern), destPattern);
            } catch (ParseException e) {
                throw new IllegalArgumentException("parse date error, str:" + data.toString() + ", pattern:" + srcPattern, e);
            }
        } else if (data instanceof byte[]) {
            value = new String((byte[]) data, DEFAULT_CHARSET);
        } else if (data instanceof char[]) {
            value = new String((char[]) data);
        } else {
            throw new IllegalArgumentException("data:" + String.valueOf(data) + " , type:" + type);
        }

        return value;
    }

    public String format2(Object data) {
        return format2(data, TYPE_STR);
    }

    public String format2(Object data, int type) {
        if (data == null) {
            return "";
        }

        String value;
        if (TYPE_STR == type && data instanceof CharSequence) {
            value = data.toString();
        } else if (TYPE_FLOAT == type || data instanceof Double || data instanceof Float || data instanceof BigDecimal) {
            value = decimalUtils.format2(data);
        } else if (TYPE_INT == type || data instanceof Integer || data instanceof Long) {
            value = data.toString();
        } else if (data instanceof Date) {
            value = dateFormat.format((Date) data);
        } else if (data instanceof Calendar) {
            value = dateFormat.format(((Calendar) data).getTime());
        } else if (data instanceof byte[]) {
            value = new String((byte[]) data, DEFAULT_CHARSET);
        } else if (data instanceof char[]) {
            value = new String((char[]) data);
        } else {
            throw new IllegalArgumentException("data:" + String.valueOf(data) + " , type:" + type);
        }

        return value;
    }

}
