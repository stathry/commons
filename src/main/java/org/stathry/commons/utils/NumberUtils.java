package org.stathry.commons.utils;

import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * NumberUtils
 * Created by dongdaiming on 2018-08-14 15:03
 */
public class NumberUtils {

    private NumberUtils() {}

    public static String divide(String num1, String num2, int scale, RoundingMode mode) {
        BigDecimal n1 = new BigDecimal(num1);
        BigDecimal n2 = new BigDecimal(num2);
        return n1.divide(n2, scale, mode).toPlainString();
    }

    public static String multiply(String num1, String num2, int scale, RoundingMode mode) {
        BigDecimal n1 = new BigDecimal(num1);
        BigDecimal n2 = new BigDecimal(num2);
        return n1.multiply(n2).setScale(scale, mode).toPlainString();
    }

    public static String add(String num1, String num2, int scale, RoundingMode mode) {
        BigDecimal n1 = new BigDecimal(num1);
        BigDecimal n2 = new BigDecimal(num2);
        return n1.add(n2).setScale(scale, mode).toPlainString();
    }

    public static String subtract(String num1, String num2, int scale, RoundingMode mode) {
        BigDecimal n1 = new BigDecimal(num1);
        BigDecimal n2 = new BigDecimal(num2);
        return n1.subtract(n2).setScale(scale, mode).toPlainString();
    }

}