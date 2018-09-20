package org.stathry.commons.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * TODO
 * Created by dongdaiming on 2018-09-07 18:28
 */
public class OrderGenerator {

    private final int length;
    private final String timePattern;
    private final String prefix;
    private final AtomicInteger sequence = new AtomicInteger();
    private final int seqLen;

    public OrderGenerator(int length, String timeFormat, String prefix) {
        this.length = length;
        this.timePattern = timeFormat;
        seqLen = length - timeFormat.length();
        this.prefix = prefix;
    }

    public static void main(String[] args) {
        OrderGenerator generator = new OrderGenerator(19, "yyyyMMddHHmm", "U");
        for (int i = 0; i < 20; i++) {
            System.out.println(generator.order());
        }
    }

    public String order() {
        StringBuilder order = new StringBuilder(prefix);
        order.append(DateFormatUtils.format(new Date(), timePattern));
        String seq = StringUtils.leftPad(String.valueOf(sequence.incrementAndGet()), seqLen, '0');
        order.append(seq);
        return order.toString();
    }

}
