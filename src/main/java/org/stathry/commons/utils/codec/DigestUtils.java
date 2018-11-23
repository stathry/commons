/**
 * Copyright 2016-2100 free Co., Ltd.
 */
package org.stathry.commons.utils.codec;

/**
 * @author dongdaiming@free.com
 * <p>
 * 2016年8月10日
 */
public final class DigestUtils {

    public static void main(String[] args) {
        System.out.println(md5("a"));
        System.out.println(System.currentTimeMillis());
        System.out.println(System.nanoTime());
    }

    private DigestUtils() {
    }

    public static String md5(String data) {
        if (data == null || data.length() == 0) {
            return "";
        }
        return org.apache.commons.codec.digest.DigestUtils.md5Hex(data);
    }

    public static String md5(byte[] data) {
        if (data == null || data.length == 0) {
            return "";
        }
        return org.apache.commons.codec.digest.DigestUtils.md5Hex(data);
    }

    public static String sha(String data) {
        if (data == null || data.length() == 0) {
            return "";
        }
        return org.apache.commons.codec.digest.DigestUtils.shaHex(data);
    }

    public static String sha(byte[] data) {
        if (data == null || data.length == 0) {
            return "";
        }
        return org.apache.commons.codec.digest.DigestUtils.shaHex(data);
    }

    public static String sha256(String data) {
        if (data == null || data.length() == 0) {
            return "";
        }
        return org.apache.commons.codec.digest.DigestUtils.sha256Hex(data);
    }

    public static String sha256(byte[] data) {
        if (data == null || data.length == 0) {
            return "";
        }
        return org.apache.commons.codec.digest.DigestUtils.sha256Hex(data);
    }

    public static String sha384(String data) {
        if (data == null || data.length() == 0) {
            return "";
        }
        return org.apache.commons.codec.digest.DigestUtils.sha384Hex(data);
    }

    public static String sha384(byte[] data) {
        if (data == null || data.length == 0) {
            return "";
        }
        return org.apache.commons.codec.digest.DigestUtils.sha384Hex(data);
    }

    public static String sha512(String data) {
        if (data == null || data.length() == 0) {
            return "";
        }
        return org.apache.commons.codec.digest.DigestUtils.sha512Hex(data);
    }

    public static String sha512(byte[] data) {
        if (data == null || data.length == 0) {
            return "";
        }
        return org.apache.commons.codec.digest.DigestUtils.sha512Hex(data);
    }

}
