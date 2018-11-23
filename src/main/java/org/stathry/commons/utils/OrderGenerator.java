package org.stathry.commons.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.util.Assert;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Random;

/**
 * OrderGenerator
 * Created by dongdaiming on 2018-09-07 18:28
 */
public class OrderGenerator {

    private static final String DEFAULT_TIME_PATTERN = "yyyyMMddHHmmssSSS";
    private final String timePattern;

    private final String prefix;

    private final int seqLen;
    private final int maxSeq;
    private final int randomLen;
    private final int maxRandom;

    private int seq;
    private Random random = new SecureRandom();

    public OrderGenerator(int orderLength) {
        this(orderLength, DEFAULT_TIME_PATTERN, "");
    }

    public OrderGenerator(int orderLength, String timeFormat) {
        this(orderLength, timeFormat, "");
    }

    public OrderGenerator(int orderLength, String timeFormat, String prefix) {
        Assert.isTrue(orderLength >= 20, "orderLength >= 20.");
        this.timePattern = timeFormat == null ? DEFAULT_TIME_PATTERN : timeFormat;
        this.prefix = prefix == null ? "" : prefix;
        randomLen = orderLength / 5;
        this.seqLen = orderLength - this.timePattern.length() - randomLen - this.prefix.length();
        this.maxSeq = (int) Math.pow(10, seqLen) - 1;
        this.maxRandom = (int) Math.pow(10, randomLen) - 1;
    }

    public String order() {
        StringBuilder order = new StringBuilder(prefix);
        order.append(DateFormatUtils.format(new Date(), timePattern));
        order.append(StringUtils.leftPad(String.valueOf(inc()), seqLen, '0'));
        order.append(StringUtils.leftPad(String.valueOf(random.nextInt(maxRandom)), randomLen, '0'));
        return order.toString();
    }

    private synchronized int inc() {
        seq = seq >= maxSeq ? 1 : (seq + 1);
        return seq;
    }

}
