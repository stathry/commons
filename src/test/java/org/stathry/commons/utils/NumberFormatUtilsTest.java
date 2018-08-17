package org.stathry.commons.utils;

import org.junit.Assert;
import org.junit.Test;

import java.math.RoundingMode;

/**
 * TODO
 * Created by dongdaiming on 2018-08-14 17:17
 */
public class NumberFormatUtilsTest {
    
    @Test
    public void testFormatScaleRoundMode() {
        // 四舍五入(负数的四舍五入相当于先去掉负号，四舍五入后再加负号)
        System.out.println(NumberFormatUtils.format(0.124, 2, RoundingMode.HALF_UP));
        Assert.assertEquals("0.12", NumberFormatUtils.format(0.124, 2, RoundingMode.HALF_UP));

        System.out.println(NumberFormatUtils.format(0.125, 2, RoundingMode.HALF_UP));
        Assert.assertEquals("0.13", NumberFormatUtils.format(0.125, 2, RoundingMode.HALF_UP));

        System.out.println(NumberFormatUtils.format(-0.125, 2, RoundingMode.HALF_UP));
        Assert.assertEquals("-0.13", NumberFormatUtils.format(-0.125, 2, RoundingMode.HALF_UP));

        // 向下截取(相当于去掉末尾多余小数位)
        System.out.println(NumberFormatUtils.format(0.125, 2, RoundingMode.DOWN));
        Assert.assertEquals("0.12", NumberFormatUtils.format(0.125, 2, RoundingMode.DOWN));

        System.out.println(NumberFormatUtils.format(-0.125, 2, RoundingMode.DOWN));
        Assert.assertEquals("-0.12", NumberFormatUtils.format(-0.125, 2, RoundingMode.DOWN));


        // 向下舍入(负数时向较小的负数舍入)
        System.out.println(NumberFormatUtils.format(0.125, 2, RoundingMode.FLOOR));
        Assert.assertEquals("0.12", NumberFormatUtils.format(0.125, 2, RoundingMode.FLOOR));

        System.out.println(NumberFormatUtils.format(-0.125, 2, RoundingMode.FLOOR));
        Assert.assertEquals("-0.13", NumberFormatUtils.format(-0.125, 2, RoundingMode.FLOOR));


        System.out.println(NumberFormatUtils.format(-0.125, 2, RoundingMode.HALF_DOWN));
        Assert.assertEquals("-0.12", NumberFormatUtils.format(-0.125, 2, RoundingMode.HALF_DOWN));

        // 银行家算法，当浮点数末尾为5时，向最近的偶数舍入
        System.out.println(NumberFormatUtils.format(0.12, 2, RoundingMode.HALF_EVEN));
        Assert.assertEquals("0.12", NumberFormatUtils.format(0.125, 2, RoundingMode.HALF_EVEN));

        System.out.println(NumberFormatUtils.format(0.125, 2, null));
        Assert.assertEquals("0.13", NumberFormatUtils.format(0.125, 2, null));

        System.out.println(NumberFormatUtils.format(0));
        Assert.assertEquals("0.00", NumberFormatUtils.format(0));

        System.out.println(NumberFormatUtils.format(0));
        Assert.assertEquals("0.00", NumberFormatUtils.format(0));

        System.out.println(NumberFormatUtils.format(0.000));
        Assert.assertEquals("0.00", NumberFormatUtils.format(0.000));

        System.out.println(NumberFormatUtils.format(0.123));
        Assert.assertEquals("0.12", NumberFormatUtils.format(0.123));

        System.out.println(NumberFormatUtils.format(0.125));
        Assert.assertEquals("0.13", NumberFormatUtils.format(0.125));

        System.out.println(NumberFormatUtils.format(0.125));
        Assert.assertEquals("1234567890.00", NumberFormatUtils.format(1234567890));
    }

    @Test
    public void testFormatScaleRoundModeGroup() {
        System.out.println(NumberFormatUtils.formatBigInt(1234567890, RoundingMode.HALF_UP, 4));
        Assert.assertEquals("12,3456,7890", NumberFormatUtils.formatBigInt(1234567890, RoundingMode.HALF_UP, 4));
    }
}
