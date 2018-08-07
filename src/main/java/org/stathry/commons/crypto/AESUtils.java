package org.stathry.commons.crypto;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * TODO
 * Created by dongdaiming on 2018-08-06 16:29
 * @see <p><a href="https://blog.csdn.net/hbcui1984/article/details/5201247">csdn</a></p>
 */
public class AESUtils {

    /**
     * 加密
     *
     * @param content 需要加密的内容
     * @param password  加密密码
     * @return
     */
    public static byte[] encrypt(String content, String password) {
        try {
            KeyGenerator kg = KeyGenerator.getInstance("AES");
            kg.init(128, new SecureRandom(password.getBytes()));
            SecretKey secretKey = kg.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            byte[] byteContent = content.getBytes("utf-8");
            cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
            return cipher.doFinal(byteContent); // 加密
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String encrypt2HexStr(String content, String password) {
        return byte2HexStr(encrypt(content, password));
    }

    /**解密
     * @param content  待解密内容
     * @param password 解密密钥
     * @return
     */
    public static byte[] decrypt(byte[] content, String password) {
        try {
            KeyGenerator kg = KeyGenerator.getInstance("AES");
            kg.init(128, new SecureRandom(password.getBytes()));
            SecretKey secretKey = kg.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
            return cipher.doFinal(content); // 加密
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decryptHexStr(String content, String password) {
        try {
            return new String(decrypt(hexStr2Byte(content), password), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String byte2HexStr(byte[] srcBytes) {
        if(srcBytes == null || srcBytes.length == 0) {
            return "";
        }
        StringBuilder hexRetSB = new StringBuilder();
        String hexString;
        for (byte b : srcBytes) {
            hexString = Integer.toHexString(0x00ff & b);
            hexRetSB.append(hexString.length() == 1 ? 0 : "").append(hexString);
        }
        return hexRetSB.toString();
    }

    /**16进制转换为二进制
     * @param hexStr
     * @return
     */
    public static byte[] hexStr2Byte(String hexStr) {
        if (hexStr == null || hexStr.isEmpty())
            return null;
        byte[] srcBytes = new byte[hexStr.length() / 2];
        for (int i = 0; i < srcBytes.length; i++) {
            srcBytes[i] = (byte) Integer.parseInt(hexStr.substring(i * 2, i * 2 + 2), 16);
        }
        return srcBytes;
    }
}
