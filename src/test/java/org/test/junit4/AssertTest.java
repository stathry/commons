package org.test.junit4;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.stathry.commons.utils.ParamNameUtils;

import java.util.Arrays;

/**
 * TestNGAssertTest
 * Created by dongdaiming on 2018-12-26 17:58
 */
public class AssertTest {

    @Test
    public void testAssert() {
        Object o = null;
        Assert.assertNull(o);
        Assert.assertNotNull(new Object());

        Assert.assertTrue(1 == 1);
        Assert.assertFalse(1 == 0);

        Assert.assertEquals("order_detail", ParamNameUtils.toUnderName("orderDetail"));
        Assert.assertNotEquals("order_detail", ParamNameUtils.toUnderName("orderdetail"));

        Assert.assertEquals(Integer.valueOf(1000), Integer.valueOf(1000));
        Assert.assertNotEquals(Integer.valueOf(1000), Integer.valueOf(1001));

        Assert.assertEquals(8, 2 * 4);
        Assert.assertNotEquals(8, 3 * 4);

        Assert.assertEquals(6.6, 6.60, 0.0);
        Assert.assertNotEquals(8, 3 * 4);

        Assert.assertEquals(6.6, 6.5, 0.1);
        Assert.assertNotEquals(6.6, 6.5, 0.01);

        Assert.assertSame(Integer.valueOf(100), Integer.valueOf(100));
        Assert.assertNotSame(Integer.valueOf(1000), Integer.valueOf(1000));
    }

    @Test
    public void testAssertThat() {
        Assert.assertThat(Arrays.asList("a", "x", "b", "y", "c"), CoreMatchers.hasItems("x", "y"));

        Assert.assertThat("abcdefxyz", CoreMatchers.allOf(CoreMatchers.startsWith("abc"), CoreMatchers.endsWith("xyz")));

        Assert.assertThat("x", CoreMatchers.anyOf(CoreMatchers.equalTo("x"), CoreMatchers.equalTo("y")));
        Assert.assertThat("y", CoreMatchers.anyOf(CoreMatchers.equalTo("x"), CoreMatchers.equalTo("y")));
    }

    @Test(expected = AssertionError.class)
    public void testAssertThatFail1() {
        Assert.assertThat(Arrays.asList("a", "x", "b", "c"), CoreMatchers.hasItems("x", "y"));
    }

    @Test(expected = AssertionError.class)
    public void testAssertThatFail2() {
        Assert.assertThat("abcdef", CoreMatchers.allOf(CoreMatchers.startsWith("abc"), CoreMatchers.endsWith("xyz")));
    }

    @Test(expected = AssertionError.class)
    public void testAssertThatFail3() {
        Assert.assertThat("z", CoreMatchers.anyOf(CoreMatchers.equalTo("x"), CoreMatchers.equalTo("y")));
    }
}
