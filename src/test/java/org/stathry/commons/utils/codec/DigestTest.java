package org.stathry.commons.utils.codec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

/**
 * TODO
 * Created by dongdaiming on 2018-08-06 11:20
 */
public class DigestTest {

    @Test
    public void testMd5Empty() {
        String r = DigestUtils.md5Hex("");
        System.out.println(r);
        Assert.assertEquals("d41d8cd98f00b204e9800998ecf8427e", r);
    }

    @Test
    public void testJdkBase64() {
        String s = java.util.Base64.getEncoder().encodeToString("拿骚N".getBytes());
        System.out.println(s);
        System.out.println(new String(java.util.Base64.getDecoder().decode(s)));
    }

    @Test
    public void testJdkBase64VSApache() {
        String s = java.util.Base64.getEncoder().encodeToString("拿骚N".getBytes());
        String s2 = Base64.encodeBase64String("拿骚N".getBytes());
        System.out.println(s);
        System.out.println(s2);
        Assert.assertEquals(s, s2);
        String r1 = new String(java.util.Base64.getDecoder().decode(s));
        String r2 = new String(Base64.decodeBase64(s2));
        System.out.println(r1);
        System.out.println(r2);
        Assert.assertEquals(r1, r2);
    }

    @Test
    public void testBase641() {
        String s = Base64Utils.encode("abc");
        System.out.println(s);
        System.out.println(Base64.encodeBase64("abc".getBytes()).length);
        assertEquals("YWJj", s);
        assertNotNull(s);
        assertNotEquals("", s);
    }

    @Test
    public void testBase642() {
        String s = Base64Utils.encode("abc");
        System.out.println(s);
        String s1 = Base64Utils.decode(s);
        System.out.println(s1);
        assertNotNull(s1);
        assertNotEquals("", s1);
    }

    @Test
    public void testShas() {
        System.out.println(DigestUtils.shaHex("a"));
        System.out.println(DigestUtils.sha256Hex("a"));
        System.out.println(DigestUtils.sha384Hex("a"));
        System.out.println(DigestUtils.sha512Hex("a"));

        String s;
        for (int i = 0; i < 100; i++) {
            s = RandomStringUtils.randomAscii(32);
            System.out.println(s);
            Assert.assertEquals(40, DigestUtils.shaHex(s).length());
            Assert.assertEquals(64, DigestUtils.sha256Hex(s).length());
            Assert.assertEquals(96, DigestUtils.sha384Hex(s).length());
            Assert.assertEquals(128, DigestUtils.sha512Hex(s).length());
        }
    }

    @Test
    public void testSha() throws Exception {
        String src = "a";
        System.out.println(DigestUtils.shaHex(src));
        MessageDigest d1 = MessageDigest.getInstance("SHA");
        MessageDigest d2 = MessageDigest.getInstance("SHA-1");
        System.out.println(d1.getAlgorithm());
        System.out.println(d2.getAlgorithm());
        byte[] a1 = d1.digest(src.getBytes());
        byte[] a2 = d2.digest(src.getBytes());
        String r1 = Hex.encodeHexString(a1);
        String r2 = Hex.encodeHexString(a2);
        System.out.println(r1);
        System.out.println(r2);
        Assert.assertEquals(r1, r2);
    }

    @Test
    public void testFileDigestCompare() throws Exception {
        String s1 = DigestUtils.md5Hex(new FileInputStream("/temp/data-original.txt"));
        String s2 = DigestUtils.md5Hex(new FileInputStream("/temp/data-original2.txt"));
        System.out.println(s1);
        System.out.println(s2);
        Assert.assertEquals(s1, s2);
    }

    @Test
    public void testFileDigest() throws Exception {
        String s1 = DigestUtils.md5Hex(new FileInputStream("/temp/data-original.txt"));
        System.out.println(s1);
    }

    @Test
    public void testStrDigest() throws Exception {
        String s1 = DigestUtils.md5Hex("black sail");
        System.out.println(s1);
        System.out.println(s1.length());
        Assert.assertEquals(32, s1.length());
    }

    @Test
    public void testSUUID() throws Exception {
        String uuid =  UUID.randomUUID().toString();
        System.out.println(uuid);
        System.out.println(uuid.length());
        Assert.assertEquals(36, uuid.length());
        uuid = uuid.replaceAll("-", "");
        System.out.println(uuid);
        System.out.println(uuid.length());
        Assert.assertEquals(32, uuid.length());
        for (int i = 0; i < 100; i++) {
            uuid = UUID.randomUUID().toString().replaceAll("-", "");
            System.out.println(uuid);
            Assert.assertEquals(32, uuid.length());
        }
    }

}
