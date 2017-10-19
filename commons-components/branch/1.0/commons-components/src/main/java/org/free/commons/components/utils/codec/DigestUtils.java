/**
 * Copyright 2016-2100 free Co., Ltd.
 */
package org.free.commons.components.utils.codec;

/**
 * @author dongdaiming@free.com
 *
 *         2016年8月10日
 */
public final class DigestUtils { 
	
	private DigestUtils() {
	}
	
	public static String md5(String data) {
		if(data == null) {
			return "";
		}
		return new String(org.apache.commons.codec.digest.DigestUtils.md5Hex(data));
	}
	
	public static String md5(byte[] data) {
		if(data == null) {
			return "";
		}
		return new String(org.apache.commons.codec.digest.DigestUtils.md5Hex(data));
	}

	public static String sha(String data) {
		if(data == null) {
			return "";
		}
		return new String(org.apache.commons.codec.digest.DigestUtils.shaHex(data));
	}
	
	public static String sha(byte[] data) {
		if(data == null) {
			return "";
		}
		return new String(org.apache.commons.codec.digest.DigestUtils.shaHex(data));
	}

	public static String sha256(String data) {
		if(data == null) {
			return "";
		}
		return new String(org.apache.commons.codec.digest.DigestUtils.sha256Hex(data));
	}
	
	public static String sha256(byte[] data) {
		if(data == null) {
			return "";
		}
		return new String(org.apache.commons.codec.digest.DigestUtils.sha256Hex(data));
	}

	public static String sha384(String data) {
		if(data == null) {
			return "";
		}
		return new String(org.apache.commons.codec.digest.DigestUtils.sha384Hex(data));
	}
	
	public static String sha384(byte[] data) {
		if(data == null) {
			return "";
		}
		return new String(org.apache.commons.codec.digest.DigestUtils.sha384Hex(data));
	}

	public static String sha512(String data) {
		if(data == null) {
			return "";
		}
		return new String(org.apache.commons.codec.digest.DigestUtils.sha512Hex(data));
	}
	
	public static String sha512(byte[] data) {
		if(data == null) {
			return "";
		}
		return new String(org.apache.commons.codec.digest.DigestUtils.sha512Hex(data));
	}

}
