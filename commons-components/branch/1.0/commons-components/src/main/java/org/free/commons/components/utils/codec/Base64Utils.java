/**
 * Copyright 2016-2100 free Co., Ltd.
 */
package org.free.commons.components.utils.codec;

import org.apache.commons.codec.binary.Base64;

/**
 * @author dongdaiming@free.com
 *
 * 2016年8月15日
 */
public final class Base64Utils {

	
	public static String decode(String data) {
		return new String(Base64.decodeBase64(data));
	}
	public static String encode(String data) {
		return Base64.encodeBase64String(data.getBytes());
	}
	
}
