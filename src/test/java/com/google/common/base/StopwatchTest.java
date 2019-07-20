package com.google.common.base;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * StopwatchTest
 *
 * @author dongdaiming(董代明)
 * @date 2019-06-27
 */
public class StopwatchTest {

    @Test
    public void test1() throws InterruptedException {
        Stopwatch watch = Stopwatch.createStarted();
       Thread.sleep(100);
        System.out.println(watch.elapsed(TimeUnit.MILLISECONDS));
        Thread.sleep(200);
        System.out.println(watch.elapsed(TimeUnit.MILLISECONDS));
        // 重新开始计时
        watch.reset().start();
        Thread.sleep(200);
        System.out.println(watch.elapsed(TimeUnit.MILLISECONDS));
    }
}
