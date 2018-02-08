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
public final class NumberUtils {

	private NumberUtils() {}
	
	public static String format(Object num) {
		return format(num, "0.00", true, 4, RoundingMode.HALF_UP);
	}
	
	public static String format(Object num,String pattern, boolean group, int size, RoundingMode mode) {
		if(num == null) {
			return null;
		}
		if (!(num instanceof Number)) {
			throw new IllegalArgumentException("num@" + num.getClass().getSimpleName());
		}
		String pattern1 = StringUtils.isBlank(pattern) ? "0.00" : pattern;
		DecimalFormat format = new DecimalFormat(pattern1);
		RoundingMode mode1 = mode == null ? RoundingMode.HALF_UP : mode;
		format.setRoundingMode(mode1);
		format.setGroupingUsed(group);
		format.setGroupingSize(size);
		return format.format(num);
	}
	
	public static String toString(Object num) {
		return toString(num, 2, RoundingMode.HALF_UP);
	}
	
	public static String toString(Object num, int scale, RoundingMode mode) {
		if(num == null) {
			return null;
		}
		if (num instanceof Number) {
			BigDecimal dec = new BigDecimal(String.valueOf(num));
			RoundingMode mode1 = mode == null ? RoundingMode.HALF_UP : mode;
			dec = dec.setScale(scale, mode1);
			return dec.toPlainString();
		}
		throw new IllegalArgumentException("num@" + num.getClass().getSimpleName());
	}
	
//	public static void main(String[] args) {
//		System.out.println(format(0.00));
//		System.out.println(format(0.000));
//		System.out.println(format(0.123));
//		System.out.println(format(0.125));
//		System.out.println(format(0));
//		System.out.println(format(12));
//		System.out.println(format(123));
//		System.out.println(format(1234));
//		System.out.println(format(12345));
//		System.out.println(format(123456789));
//		System.out.println(format(new Date(), null));

//		System.out.println(toString(0.00));
//		System.out.println(toString(0.000, 2, null));
//		System.out.println(toString(0.123, 2, null));
//		System.out.println(toString(0.125, 2, null));
//		System.out.println(toString(0, 2, null));
//		System.out.println(toString(123456789, 2, null));
//		System.out.println(format2(new Date(), 2, null));
//	}
}
