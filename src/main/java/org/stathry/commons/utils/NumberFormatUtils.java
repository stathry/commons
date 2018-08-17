/*
 * Copyright © stathry@126.com All Rights Reserved
 */
package org.stathry.commons.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import org.apache.commons.lang3.StringUtils;

/**
 * @author stathry@126.com
 * @date 2017年4月13日
 */
public final class NumberFormatUtils {

	private NumberFormatUtils() {}

    public static String format(Number num) {
        return format(num, 2, RoundingMode.HALF_UP);
    }

    public static String format(Number num, int scale, RoundingMode mode) {
        if(num == null) {
            return "";
        }
        BigDecimal dec = new BigDecimal(num.toString());
        mode = mode == null ? RoundingMode.HALF_UP : mode;
        dec = dec.setScale(scale, mode);
        return dec.toPlainString();
    }

    public static String formatBigInt(Number num, RoundingMode mode, int groupSize) {
        if(num == null) {
            return "";
        }
        DecimalFormat format = new DecimalFormat();
        mode = mode == null ? RoundingMode.HALF_UP : mode;
        format.setRoundingMode(mode);
        format.setGroupingUsed(true);
        format.setGroupingSize(groupSize);
        return format.format(num);
    }
	
	public static String formatByPattern(Number num) {
		return formatByPattern(num, "0.00", true, 4, RoundingMode.HALF_UP);
	}
	
	public static String formatByPattern(Number num,String pattern, boolean group, int size, RoundingMode mode) {
		if(num == null || StringUtils.isBlank(pattern)) {
			return "";
		}
		String pattern1 = StringUtils.isBlank(pattern) ? "0.00" : pattern;
		DecimalFormat format = new DecimalFormat(pattern1);
		mode = mode == null ? RoundingMode.HALF_UP : mode;
		format.setRoundingMode(mode);
		format.setGroupingUsed(group);
		format.setGroupingSize(size);
		return format.format(num);
	}

}
