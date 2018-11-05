package org.stathry.commons.utils;

import org.junit.Assert;
import org.junit.Test;

import java.math.RoundingMode;

/**
 * TODO
 * Created by dongdaiming on 2018-08-14 17:17
 */
public class DecimalUtilsTest {
    
    @Test
    public void testFormat1() {
        // DecimalUtils.formatFraction
        Assert.assertTrue("500.89".equals(DecimalUtils.format(500.885, 2, RoundingMode.HALF_UP)));
        Assert.assertTrue("500.89".equals(DecimalUtils.format(500.885f, 2, RoundingMode.HALF_UP)));

        Assert.assertTrue("6.00".equals(DecimalUtils.format(6, 2, RoundingMode.HALF_UP)));
        Assert.assertTrue("-6.00".equals(DecimalUtils.format(-6, 2, RoundingMode.HALF_UP)));

        Assert.assertTrue("6.60".equals(DecimalUtils.format(6.6, 2, RoundingMode.HALF_UP)));
        Assert.assertTrue("-6.60".equals(DecimalUtils.format(-6.6, 2, RoundingMode.HALF_UP)));

        Assert.assertTrue("1.66".equals(DecimalUtils.format(1.656, 2, RoundingMode.HALF_UP)));
        Assert.assertTrue("1.65".equals(DecimalUtils.format(1.654, 2, RoundingMode.HALF_UP)));
        Assert.assertTrue("-1.66".equals(DecimalUtils.format(-1.656, 2, RoundingMode.HALF_UP)));
        Assert.assertTrue("-1.65".equals(DecimalUtils.format(-1.654, 2, RoundingMode.HALF_UP)));

        Assert.assertTrue("6.60".equals(DecimalUtils.format("6.6", 2, RoundingMode.HALF_UP)));
        Assert.assertTrue("-6.60".equals(DecimalUtils.format("-6.6", 2, RoundingMode.HALF_UP)));

        Assert.assertTrue("1.66".equals(DecimalUtils.format("1.656", 2, RoundingMode.HALF_UP)));
        Assert.assertTrue("-1.66".equals(DecimalUtils.format("-1.656", 2, RoundingMode.HALF_UP)));
    }

    @Test
    public void testFormatScaleRoundMode() {
        // 四舍五入(负数的四舍五入相当于先去掉负号，四舍五入后再加负号)
        Assert.assertEquals("0.12", DecimalUtils.format(0.124, 2, RoundingMode.HALF_UP));

        Assert.assertEquals("0.13", DecimalUtils.format(0.125, 2, RoundingMode.HALF_UP));

        Assert.assertEquals("-0.13", DecimalUtils.format(-0.125, 2, RoundingMode.HALF_UP));

        // 向下截取(相当于去掉末尾多余小数位)
        Assert.assertEquals("0.12", DecimalUtils.format(0.125, 2, RoundingMode.DOWN));

        Assert.assertEquals("-0.12", DecimalUtils.format(-0.125, 2, RoundingMode.DOWN));

        // 向下舍入(负数时向较小的负数舍入)
        Assert.assertEquals("0.12", DecimalUtils.format(0.125, 2, RoundingMode.FLOOR));

        Assert.assertEquals("-0.13", DecimalUtils.format(-0.125, 2, RoundingMode.FLOOR));

        Assert.assertEquals("-0.12", DecimalUtils.format(-0.125, 2, RoundingMode.HALF_DOWN));

        // 银行家算法，当浮点数末尾为5时，向最近的偶数舍入
        Assert.assertEquals("0.12", DecimalUtils.format(0.125, 2, RoundingMode.HALF_EVEN));

        Assert.assertEquals("0.13", DecimalUtils.format(0.125, 2, RoundingMode.HALF_UP));

        Assert.assertEquals("0.00", DecimalUtils.format(0, 2, RoundingMode.HALF_UP));

        Assert.assertEquals("0.00", DecimalUtils.format(0.000, 2, RoundingMode.HALF_UP));

        Assert.assertEquals("0.12", DecimalUtils.format(0.123, 2, RoundingMode.HALF_UP));

        Assert.assertEquals("0.13", DecimalUtils.format(0.125, 2, RoundingMode.HALF_UP));
        Assert.assertEquals("456.46", DecimalUtils.format(456.456, 2, RoundingMode.HALF_UP));

        Assert.assertEquals("1234567890123456.00", DecimalUtils.format(1234567890123456L, 2, RoundingMode.HALF_UP));

        Assert.assertEquals("12345678901234.46", DecimalUtils.format(12345678901234.456, 2, RoundingMode.HALF_UP));
    }
}
