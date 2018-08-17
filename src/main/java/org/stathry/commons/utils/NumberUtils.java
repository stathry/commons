package org.stathry.commons.utils;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * NumberUtils
 * Created by dongdaiming on 2018-08-14 15:03
 */
public class NumberUtils {

    private NumberUtils() {}

    public static String format(double num, int scale, RoundingMode mode) {
        DecimalFormat f = new DecimalFormat();
        f.setMaximumFractionDigits(scale);
        f.setMinimumFractionDigits(scale);
        f.setRoundingMode(mode);
        return f.format(num);
    }

    public static String downFormat(double num, int scale) {
        return format(num, scale, RoundingMode.DOWN);
    }

    public static String downFormat(String num, int scale) {
        return format(Double.valueOf(num), scale, RoundingMode.DOWN);
    }

    public static String format(String num, int scale, RoundingMode mode) {
        return format(Double.valueOf(num), scale, mode);
    }

  /*  public static void main(String[] args) {
        // downFormat
        Assert.isTrue("6.00".equals(downFormat(6, 2)), "error.");
        Assert.isTrue("-6.00".equals(downFormat(-6, 2)), "error.");

        Assert.isTrue("6.60".equals(downFormat(6.6, 2)), "error.");
        Assert.isTrue("-6.60".equals(downFormat(-6.6, 2)), "error.");

        Assert.isTrue("1.65".equals(downFormat(1.656, 2)), "error.");
        Assert.isTrue("1.65".equals(downFormat(1.654, 2)), "error.");
        Assert.isTrue("-1.65".equals(downFormat(-1.656, 2)), "error.");
        Assert.isTrue("-1.65".equals(downFormat(-1.654, 2)), "error.");

        Assert.isTrue("6.00".equals(downFormat("6", 2)), "error.");
        Assert.isTrue("-6.00".equals(downFormat("-6", 2)), "error.");

        Assert.isTrue("6.60".equals(downFormat("6.6", 2)), "error.");
        Assert.isTrue("-6.60".equals(downFormat("-6.6", 2)), "error.");

        Assert.isTrue("1.65".equals(downFormat("1.656", 2)), "error.");
        Assert.isTrue("-1.65".equals(downFormat("-1.656", 2)), "error.");

        // format
        Assert.isTrue("6.00".equals(format(6, 2, RoundingMode.HALF_UP)), "error.");
        Assert.isTrue("-6.00".equals(format(-6, 2, RoundingMode.HALF_UP)), "error.");

        Assert.isTrue("6.60".equals(format(6.6, 2, RoundingMode.HALF_UP)), "error.");
        Assert.isTrue("-6.60".equals(format(-6.6, 2, RoundingMode.HALF_UP)), "error.");

        Assert.isTrue("1.66".equals(format(1.656, 2, RoundingMode.HALF_UP)), "error.");
        Assert.isTrue("1.65".equals(format(1.654, 2, RoundingMode.HALF_UP)), "error.");
        Assert.isTrue("-1.66".equals(format(-1.656, 2, RoundingMode.HALF_UP)), "error.");
        Assert.isTrue("-1.65".equals(format(-1.654, 2, RoundingMode.HALF_UP)), "error.");


        Assert.isTrue("6.60".equals(format("6.6", 2, RoundingMode.HALF_UP)), "error.");
        Assert.isTrue("-6.60".equals(format("-6.6", 2, RoundingMode.HALF_UP)), "error.");

        Assert.isTrue("1.66".equals(format("1.656", 2, RoundingMode.HALF_UP)), "error.");
        Assert.isTrue("-1.66".equals(format("-1.656", 2, RoundingMode.HALF_UP)), "error.");

    }*/

}
