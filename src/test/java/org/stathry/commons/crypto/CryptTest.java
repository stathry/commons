package org.stathry.commons.crypto;

import org.junit.Assert;
import org.junit.Test;

import java.io.UnsupportedEncodingException;

/**
 * TODO
 * Created by dongdaiming on 2018-08-06 16:35
 */
public class CryptTest {

    @Test
    public void testEncryptByAES1() throws UnsupportedEncodingException {
        String pwd = "xyz";
        String src = "abc";
        byte[] data = AESUtils.encrypt(src, pwd);
        System.out.println(data.length);
        System.out.println("encrypted:" + AESUtils.byte2HexStr(data));

        String r = new String(AESUtils.decrypt(data, pwd), "utf-8");
        System.out.println("decrypted:" + r);
        Assert.assertEquals(src, r);
    }

    @Test
    public void testEncryptByAES2() throws UnsupportedEncodingException {
        String pwd = "xyz";
        String src = "tianji_api_tianjireport_detail_response";
        String data = AESUtils.encrypt2HexStr(src, pwd);
        System.out.println("encrypted:" + data);

        String r = AESUtils.decryptHexStr(data, pwd);
        System.out.println("decrypted:" + r);
        Assert.assertEquals(src, r);
    }
    @Test
    public void testEncryptByAES3() {
        String pwd = "xyz";
        String src = "tianji_api_tianjireport_detail_response";
        String data1 = AESUtils.encrypt2HexStr(src, pwd);
        String data2 = BaofooSecurityUtil.aesEncrypt(src, pwd);
        System.out.println("encrypted data1:" + data1);
        System.out.println("encrypted data2:" + data2);
        Assert.assertEquals(data1, data2);
        String r1 = AESUtils.decryptHexStr(data1, pwd);
        String r2 = BaofooSecurityUtil.aesDecrypt(data2, pwd);
        Assert.assertEquals(r1, r2);
    }

    @Test
    public void testEncryptByAESPFM() throws UnsupportedEncodingException {
        String pwd = "xyz";
        String src = "tianji_api_tianjireport_detail_response";
        int limit = 10000;
        String data = "";
        long begin = System.currentTimeMillis();
        for (int i = 0; i < limit; i++) {
            data = AESUtils.encrypt2HexStr(src + i, pwd);
        }
        System.out.println(System.currentTimeMillis() - begin);
        long time = System.currentTimeMillis() - begin;
        System.out.println("limit:" + limit + ", time:" + time + " ms.");
    }

}
