/*
 * Copyright © stathry@126.com All Rights Reserved
 */
package org.stathry.commons.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

/**
 * @author stathry@126.com
 * @date 2017年4月13日
 */
public final class NumberFormatUtils {

	private NumberFormatUtils() {}

    public static String format(Number num, int scale, RoundingMode mode) {
        DecimalFormat f = new DecimalFormat();
        f.setGroupingUsed(false);
        f.setMaximumFractionDigits(scale);
        f.setMinimumFractionDigits(scale);
        f.setRoundingMode(mode);
        return f.format(num);
    }

    public static String downFormat(Number num, int scale) {
        return format(num, scale, RoundingMode.DOWN);
    }

    public static String downFormat(String num, int scale) {
        return format(Double.valueOf(num), scale, RoundingMode.DOWN);
    }

    public static String format(String num, int scale, RoundingMode mode) {
        return format(Double.valueOf(num), scale, mode);
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