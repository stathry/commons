package org.apache.commons.lang3;

import org.junit.Assert;
import org.junit.Test;
import org.stathry.commons.utils.NumberUtils;

import java.util.Date;

/**
 * TODO
 * Created by dongdaiming on 2018-11-06 15:28
 */
public class StringUtilsTest {

    @Test
    public void testIsNum() {
        Assert.assertTrue(StringUtils.isNumeric("666"));
        Assert.assertFalse(StringUtils.isNumeric("6.66"));
        Assert.assertTrue(StringUtils.isNumeric("000666"));

        Assert.assertTrue(NumberUtils.isNum("666"));
        Assert.assertTrue(NumberUtils.isNum("6.66"));
        Assert.assertTrue(NumberUtils.isNum("0066"));
        Assert.assertTrue(NumberUtils.isNum(" 0066 "));

        Assert.assertFalse(NumberUtils.isNum("00 66"));
    }

    @Test
    public void testCommon() {
        Assert.assertEquals("cba", StringUtils.reverse("abc"));
        Assert.assertEquals("abc", StringUtils.truncate("abcdef", 3));
        Assert.assertEquals(" ab ", StringUtils.center("ab", 4));
        Assert.assertEquals(2, StringUtils.countMatches("abcba", 'a'));
        Assert.assertEquals("abc", StringUtils.deleteWhitespace("   ab  c  "));
        Assert.assertEquals("2018110600", StringUtils.rightPad("20181106", 10, '0'));
        Assert.assertEquals("abcabc", StringUtils.repeat("abc", 2));
        Assert.assertEquals("0abc0", StringUtils.wrap("abc", '0'));

        System.out.println(StringUtils.join("今天是", new Date()));
        Assert.assertEquals("今年是2018", StringUtils.join("今年是", "2018"));
        Assert.assertEquals("1,2,3", StringUtils.join(new int[]{1, 2, 3}, ','));
    }
}
