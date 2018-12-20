package org.stathry.commons.log;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * TODO
 * Created by dongdaiming on 2018-08-07 10:12
 */
public class LogTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogTest.class);
    private static final Logger BIZ_LOG1 = LoggerFactory.getLogger("biz1Log");

    @Test
    public void testLogToCustFile() {
        BIZ_LOG1.debug("hello, {}.", new MSG());// 无需输出时不会把参数转换成String,即不会执行toString

        BIZ_LOG1.info("hello, {}.", "flint");
        BIZ_LOG1.info("hello, {}, {}, {}.", "flint", 666, new Date());

        BIZ_LOG1.warn("warn, {}.", "flint");
        BIZ_LOG1.warn("warn, {}, {}, {}, {}, {}.", "flint", 666, 4, 5, new Date());
    }

    @Test
    public void test1() {
        LOGGER.debug("hello, {}.", new MSG());// 无需输出时不会把参数转换成String,即不会执行toString

        LOGGER.info("hello, {}.", "flint");
        LOGGER.info("hello, {}, {}, {}.", "flint", 666, new Date());

        LOGGER.warn("warn, {}.", "flint");
        LOGGER.warn("warn, {}, {}, {}, {}, {}.", "flint", 666, 4, 5, new Date());
    }

    private static class MSG {

        public MSG() {
            System.out.println("MSG:CONSTRUCTOR");

        }

        @Override
        public String toString() {
            System.out.println("MGS:formatFraction");
            return super.toString();
        }
    }

    @Test
    public void testConc() throws InterruptedException {
        int tn = 8;
        int limit = 4_0000;
        ExecutorService exec = Executors.newFixedThreadPool(tn);
        for (int i = 0; i < tn; i++) {
            exec.execute(() -> {
                for (int j = 0; j < limit; j++) {
                    LOGGER.warn("hello, {}.", j);
                }
            });
        }
        exec.shutdown();
        exec.awaitTermination(5, TimeUnit.MINUTES);
    }
}
