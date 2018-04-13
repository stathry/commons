package org.apache.commons.lang3;

import org.junit.Test;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;

/**
 * TODO
 * 
 * @author dongdaiming
 * @date 2017年12月6日
 */
public class RegExpTest {

    private static final String REGEXP = "\\d*[02468]";
    private static final int TN = 8;
    private static final int LIMIT = 1000000;
    private static final String ARR = "ABC,DEF,XYZ";
    private static final char SEP = ',';

    // 1370,1438,1477
    @Test
    public void testSplit() throws InterruptedException {
        long start = System.currentTimeMillis();
        LongAdder counter = new LongAdder();
        ExecutorService exec = Executors.newFixedThreadPool(TN);
        for (int i = 0; i < TN; i++) {
            exec.submit(() -> {
                for (int j = 0; j < LIMIT; j++) {
                    String s = ARR + j;
                    counter.add(s.split(",").length);
                }
            });
        }
        exec.shutdown();
        exec.awaitTermination(3, TimeUnit.MINUTES);
        long end = System.currentTimeMillis();
        System.out.println(end - start);
        System.out.println(counter.longValue());
    }

    // 1267,1235,1397
    @Test
    public void testApacheSplit() throws InterruptedException {
        long start = System.currentTimeMillis();
        LongAdder counter = new LongAdder();
        ExecutorService exec = Executors.newFixedThreadPool(TN);
        for (int i = 0; i < TN; i++) {
            exec.submit(() -> {
                for (int j = 0; j < LIMIT; j++) {
                    String s = ARR + j;
                    counter.add(StringUtils.split(s, ',').length);
                }
            });
        }
        exec.shutdown();
        exec.awaitTermination(3, TimeUnit.MINUTES);
        long end = System.currentTimeMillis();
        System.out.println(end - start);
        System.out.println(counter.longValue());
    }

}
