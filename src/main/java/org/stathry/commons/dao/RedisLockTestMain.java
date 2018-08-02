package org.stathry.commons.dao;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.Assert;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.stathry.commons.lock.DistributedLock;
import org.stathry.commons.lock.RedisLock;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * TODO
 * Created by dongdaiming on 2018-07-23 19:23
 */
public class RedisLockTestMain {

    public static void main(String[] args) throws InterruptedException {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:spring-context.xml");
        ctx.start();
        RedisTemplate<String, Long> redisTemplate = ctx.getBean("redisTemplate", RedisTemplate.class);
        long start = System.currentTimeMillis();
        int limit = 10_0000;
        Inc inc = new Inc();
        String key = DateFormatUtils.format(new Date(), "yyyyMMddHHmmssSSS");
        DistributedLock lock = new RedisLock(redisTemplate, key);
        System.out.println(key);
        redisTemplate.delete(key);
        int tn = 2;
        ExecutorService exec = Executors.newFixedThreadPool(tn);
        for (int i = 0; i < tn; i++) {
            exec.execute(() -> {
                for (int j = 0; j < limit; j++) {
                    try {
                        if (lock.lock()) {
                            inc.inc();
                            lock.unlock();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        exec.shutdown();
        exec.awaitTermination(5, TimeUnit.MINUTES);
        Assert.assertEquals(tn * limit, inc.inc() - 1);
        System.out.println("times:" + tn * limit + ", sec:" + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - start));
    }


    private static class Inc {

        private int i = 0;
        public int inc() {
            return ++i;
        }
    }

}
