package org.stathry.commons.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;

/**
 * OrderGenerator
 * Created by dongdaiming on 2018-09-07 18:28
 */
public class OrderGenerator {

    private final String timePattern;
    
    private final String prefix;
    
    private final int seqLen;
    private final int maxSeq;
    
    private int seq;
    
    public OrderGenerator(int orderLength, String timeFormat, String prefix) {
        this.timePattern = timeFormat;
        this.prefix = prefix;
        this.seqLen = orderLength - timeFormat.length() - prefix.length();
        this.maxSeq = (int)Math.pow(10, seqLen) - 1;
    }

    public static void main(String[] args) throws InterruptedException {
        OrderGenerator generator = new OrderGenerator(20, "yyyyMMddHHmm", "U");
//        for (int i = 0; i < 20; i++) {
//            System.out.println(generator.order());
//        }

//        int tn = 4, limit = 100_0000;
//        ExecutorService exec = Executors.newFixedThreadPool(tn);
//        List<String> list = new Vector<>(tn * limit);
//        long start = System.currentTimeMillis();
//        for (int i = 0; i < tn; i++) {
//            exec.execute(() -> {
//                for (int j = 0; j < limit; j++) {
//                    list.add(generator.order());
//                }
//            });
//        }
//
//        exec.shutdown();
//        exec.awaitTermination(3, TimeUnit.MINUTES);
//        Set<String> set = new HashSet<>(list);
//        System.out.println(set.size() + " expected size " + tn * limit);
//        Assert.isTrue(set.size() == tn * limit, "error");
//
//        String order;
//        for (int i = 0, size = list.size(); i < size; i++) {
//            order = list.get(i);
//            if(i % 100 == 0) {
////            System.out.println(order + "\t" + order.length());
//            }
//            Assert.isTrue(order.length() == 20, "error");
//        }
//        System.out.println(System.currentTimeMillis() - start);
    }

    public String order() {
        StringBuilder order = new StringBuilder(prefix);
        order.append(DateFormatUtils.format(new Date(), timePattern));
        order.append(StringUtils.leftPad(String.valueOf(inc()), seqLen, '0'));
        return order.toString();
    }

    private synchronized int inc() {
        seq = seq >= maxSeq ? 1 : (seq + 1);
        return seq;
    }

}
