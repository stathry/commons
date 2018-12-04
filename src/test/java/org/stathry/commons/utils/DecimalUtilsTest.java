package org.stathry.commons.utils;

import org.junit.Assert;
import org.junit.Test;

import java.math.RoundingMode;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * TODO
 * Created by dongdaiming on 2018-08-14 17:17
 */
public class DecimalUtilsTest {

    @Test
    public void testStaticFormat() {
        Assert.assertEquals("500.89", DecimalUtils.format(500.885, 2, RoundingMode.HALF_UP));
        Assert.assertEquals("500.89", DecimalUtils.format(500.885f, 2, RoundingMode.HALF_UP));

        Assert.assertEquals("6.00", DecimalUtils.format(6, 2, RoundingMode.HALF_UP));
        Assert.assertEquals("-6.00", DecimalUtils.format(-6, 2, RoundingMode.HALF_UP));

        Assert.assertEquals("6.60", DecimalUtils.format(6.6, 2, RoundingMode.HALF_UP));
        Assert.assertEquals("-6.60", DecimalUtils.format(-6.6, 2, RoundingMode.HALF_UP));

        Assert.assertEquals("1.66", DecimalUtils.format(1.656, 2, RoundingMode.HALF_UP));
        Assert.assertEquals("1.65", DecimalUtils.format(1.654, 2, RoundingMode.HALF_UP));
        Assert.assertEquals("-1.66", DecimalUtils.format(-1.656, 2, RoundingMode.HALF_UP));
        Assert.assertEquals("-1.65", DecimalUtils.format(-1.654, 2, RoundingMode.HALF_UP));

        Assert.assertEquals("6.60", DecimalUtils.format("6.6", 2, RoundingMode.HALF_UP));
        Assert.assertEquals("-6.60", DecimalUtils.format("-6.6", 2, RoundingMode.HALF_UP));

        Assert.assertEquals("1.66", DecimalUtils.format("1.656", 2, RoundingMode.HALF_UP));
        Assert.assertEquals("-1.66", DecimalUtils.format("-1.656", 2, RoundingMode.HALF_UP));
    }

    @Test
    public void testNewFormat() throws InterruptedException {
        DecimalUtils FORMATTER = new DecimalUtils();
        ExecutorService exec = Executors.newFixedThreadPool(4);
        for (int n = 0; n < 4; n++) {
            exec.execute(() -> {
                long start = System.currentTimeMillis();
                for (int i = 0; i < 100_0000; i++) {

                    Assert.assertEquals("500.89", FORMATTER.format(500.885, 2, RoundingMode.HALF_UP));
                    Assert.assertEquals("500.89", FORMATTER.format(500.885f, 2, RoundingMode.HALF_UP));

                    Assert.assertEquals("6.00", FORMATTER.format(6, 2, RoundingMode.HALF_UP));
                    Assert.assertEquals("-6.00", FORMATTER.format(-6, 2, RoundingMode.HALF_UP));

                    Assert.assertEquals("6.60", FORMATTER.format(6.6, 2, RoundingMode.HALF_UP));
                    Assert.assertEquals("-6.60", FORMATTER.format(-6.6, 2, RoundingMode.HALF_UP));

                    Assert.assertEquals("1.66", FORMATTER.format(1.656, 2, RoundingMode.HALF_UP));
                    Assert.assertEquals("1.65", FORMATTER.format(1.654, 2, RoundingMode.HALF_UP));
                    Assert.assertEquals("-1.66", FORMATTER.format(-1.656, 2, RoundingMode.HALF_UP));
                    Assert.assertEquals("-1.65", FORMATTER.format(-1.654, 2, RoundingMode.HALF_UP));

                    Assert.assertEquals("6.60", FORMATTER.format("6.6", 2, RoundingMode.HALF_UP));
                    Assert.assertEquals("-6.60", FORMATTER.format("-6.6", 2, RoundingMode.HALF_UP));

                    Assert.assertEquals("1.66", FORMATTER.format("1.656", 2, RoundingMode.HALF_UP));
                    Assert.assertEquals("-1.66", FORMATTER.format("-1.656", 2, RoundingMode.HALF_UP));
                }
                System.out.println(Thread.currentThread().getName() + ", done. time " + (System.currentTimeMillis() - start));
            });
        }

        exec.shutdown();
        exec.awaitTermination(5, TimeUnit.MINUTES);
    }

    @Test
    public void testStaticRoundFormat() {
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
