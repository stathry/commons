/**
 * Copyright 2012-2016 free Co., Ltd.
 */
package org.stathry.commons.utils;

import java.util.Date;

/**
 * @author dongdaiming@free.com 2016年8月23日
 */
public final class TextUtils {
	
	private TextUtils() {}

	/**
	 * 获取基本数据类型等的string格式的值
	 * 
	 * @param data
	 * @return
	 */
	public static String toString(Object data) {
		if (data == null) {
			return null;
		}

		if (data instanceof String) {
			return (String) data;
		}
		if (data instanceof Number) {
			return NumberFormatUtils.format((Number) data);
		}

		if (data instanceof byte[]) {
			byte[] temp = (byte[]) data;
			return new String(temp);
		}

		if (data instanceof char[]) {
			char[] temp = (char[]) data;
			return new String(temp);
		}

		if (data instanceof Date) {
			Date temp = (Date) data;
			return DatetimeFormatUtils.format(temp);
		}

		return data.toString();
	}

	/**
	 * 获取字符串编码
	 * 
	 * @param str
	 * @return
	 */
	public static String getEncoding(String str) {
		String encode = "ISO-8859-1";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) {
				String s1 = encode;
				return s1;
			}
		} catch (Exception exception1) {
		}
		encode = "UTF-8";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) {
				String s2 = encode;
				return s2;
			}
		} catch (Exception exception2) {
		}
		encode = "GBK";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) {
				String s3 = encode;
				return s3;
			}
		} catch (Exception exception3) {
		}
		encode = "GB2312";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) {
				String s = encode;
				return s;
			}
		} catch (Exception exception) {
		}
		return "";
	}

	public static void main(String[] args) {
		System.out.println(getEncoding("中国"));
		System.out.println(toString(new Date()));
		System.out.println(toString(1223333.323f));
	}

}
