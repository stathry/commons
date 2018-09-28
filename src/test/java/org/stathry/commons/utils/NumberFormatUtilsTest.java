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
    public void testFormat1() {
        // NumberFormatUtils.formatFraction
        org.springframework.util.Assert.isTrue("500.89".equals(NumberFormatUtils.format(500.885, 2, RoundingMode.HALF_UP)), "error.");
        org.springframework.util.Assert.isTrue("500.89".equals(NumberFormatUtils.format(500.885f, 2, RoundingMode.HALF_UP)), "error.");

        org.springframework.util.Assert.isTrue("6.00".equals(NumberFormatUtils.format(6, 2, RoundingMode.HALF_UP)), "error.");
        org.springframework.util.Assert.isTrue("-6.00".equals(NumberFormatUtils.format(-6, 2, RoundingMode.HALF_UP)), "error.");

        org.springframework.util.Assert.isTrue("6.60".equals(NumberFormatUtils.format(6.6, 2, RoundingMode.HALF_UP)), "error.");
        org.springframework.util.Assert.isTrue("-6.60".equals(NumberFormatUtils.format(-6.6, 2, RoundingMode.HALF_UP)), "error.");

        org.springframework.util.Assert.isTrue("1.66".equals(NumberFormatUtils.format(1.656, 2, RoundingMode.HALF_UP)), "error.");
        org.springframework.util.Assert.isTrue("1.65".equals(NumberFormatUtils.format(1.654, 2, RoundingMode.HALF_UP)), "error.");
        org.springframework.util.Assert.isTrue("-1.66".equals(NumberFormatUtils.format(-1.656, 2, RoundingMode.HALF_UP)), "error.");
        org.springframework.util.Assert.isTrue("-1.65".equals(NumberFormatUtils.format(-1.654, 2, RoundingMode.HALF_UP)), "error.");

        org.springframework.util.Assert.isTrue("6.60".equals(NumberFormatUtils.format("6.6", 2, RoundingMode.HALF_UP)), "error.");
        org.springframework.util.Assert.isTrue("-6.60".equals(NumberFormatUtils.format("-6.6", 2, RoundingMode.HALF_UP)), "error.");

        org.springframework.util.Assert.isTrue("1.66".equals(NumberFormatUtils.format("1.656", 2, RoundingMode.HALF_UP)), "error.");
        org.springframework.util.Assert.isTrue("-1.66".equals(NumberFormatUtils.format("-1.656", 2, RoundingMode.HALF_UP)), "error.");
    }
    
    @Test
    public void testFormatScaleRoundMode() {
        // 四舍五入(负数的四舍五入相当于先去掉负号，四舍五入后再加负号)
        Assert.assertEquals("0.12", NumberFormatUtils.format(0.124, 2, RoundingMode.HALF_UP));

        Assert.assertEquals("0.13", NumberFormatUtils.format(0.125, 2, RoundingMode.HALF_UP));

        Assert.assertEquals("-0.13", NumberFormatUtils.format(-0.125, 2, RoundingMode.HALF_UP));

        // 向下截取(相当于去掉末尾多余小数位)
        Assert.assertEquals("0.12", NumberFormatUtils.format(0.125, 2, RoundingMode.DOWN));

        Assert.assertEquals("-0.12", NumberFormatUtils.format(-0.125, 2, RoundingMode.DOWN));

        // 向下舍入(负数时向较小的负数舍入)
        Assert.assertEquals("0.12", NumberFormatUtils.format(0.125, 2, RoundingMode.FLOOR));

        Assert.assertEquals("-0.13", NumberFormatUtils.format(-0.125, 2, RoundingMode.FLOOR));

        Assert.assertEquals("-0.12", NumberFormatUtils.format(-0.125, 2, RoundingMode.HALF_DOWN));

        // 银行家算法，当浮点数末尾为5时，向最近的偶数舍入
        Assert.assertEquals("0.12", NumberFormatUtils.format(0.125, 2, RoundingMode.HALF_EVEN));

        Assert.assertEquals("0.13", NumberFormatUtils.format(0.125, 2, RoundingMode.HALF_UP));

        Assert.assertEquals("0.00", NumberFormatUtils.format(0, 2, RoundingMode.HALF_UP));

        Assert.assertEquals("0.00", NumberFormatUtils.format(0.000, 2, RoundingMode.HALF_UP));

        Assert.assertEquals("0.12", NumberFormatUtils.format(0.123, 2, RoundingMode.HALF_UP));

        Assert.assertEquals("0.13", NumberFormatUtils.format(0.125, 2, RoundingMode.HALF_UP));
        Assert.assertEquals("456.46", NumberFormatUtils.format(456.456, 2, RoundingMode.HALF_UP));

        Assert.assertEquals("1234567890123456.00", NumberFormatUtils.format(1234567890123456L, 2, RoundingMode.HALF_UP));

        Assert.assertEquals("12345678901234.46", NumberFormatUtils.format(12345678901234.456, 2, RoundingMode.HALF_UP));
    }

    @Test
    public void testFormatScaleRoundModeGroup() {
        Assert.assertEquals("1234,5678,9012,3456", NumberFormatUtils.formatBigInt(1234567890123456L, RoundingMode.HALF_UP, 4));
        Assert.assertEquals("0000,1234", NumberFormatUtils.formatBigInt(1234L, RoundingMode.HALF_UP, 4, 8));
        Assert.assertEquals("1234,5678,9012,3456", NumberFormatUtils.formatBigInt(1234567890123456L, RoundingMode.HALF_UP, 4, 8));
    }
}
