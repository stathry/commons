package org.stathry.commons.utils.codec;

import com.google.common.math.IntMath;
import com.google.common.primitives.Ints;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileInputStream;
import java.util.UUID;

/**
 * TODO
 * Created by dongdaiming on 2018-08-06 11:20
 */
public class DigestUtilsTest {

    @Test
    public void test11() {
        System.out.println(DigestUtils.sha256Hex("a"));
        System.out.println(DigestUtils.sha384Hex("b"));
        System.out.println(DigestUtils.sha512Hex("c"));
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
