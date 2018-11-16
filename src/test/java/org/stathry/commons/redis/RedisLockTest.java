package org.stathry.commons.redis;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
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
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-context.xml")
public class RedisLockTest {

    @Autowired
    protected RedisTemplate<String, Long> redisTemplate;

    @Test
    public void testSetNXAndExpire() {
        String key = DateFormatUtils.format(new Date(), "yyyyMMddHHmmssSSS");
        if(redisTemplate.opsForValue().setIfAbsent(key, 1L)) {
            redisTemplate.expire(key, 30, TimeUnit.SECONDS);
        }
    }

    @Test
    public void testOriginalSingleInc() throws InterruptedException {
        int limit = 1_0000;
        Inc inc = new Inc();
        String key = "k_inc";
        ValueOperations<String, Long> ops = redisTemplate.opsForValue();
        for (int i = 0; i < limit; i++) {
            if (ops.setIfAbsent(key, 1L)) {
                inc.inc();
                redisTemplate.delete(key);
            }
        }
        Assert.assertEquals(limit, inc.inc() - 1);
    }

    @Test
    public void testSingleInc() throws InterruptedException {
        long start = System.currentTimeMillis();
        int limit = 10_0000;
        Inc inc = new Inc();
        String key = "dist1:" + DateFormatUtils.format(new Date(), "yyyyMMddHHmmssSSS");
        DistributedLock lock = new RedisLock(key);
        redisTemplate.delete(key);
        for (int i = 0; i < limit; i++) {
            if (lock.lock()) {
                inc.inc();
                lock.unlock();
            }
        }
        System.out.println("limit:" + limit + ", sec:" + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - start));
        Assert.assertEquals(limit, inc.inc() - 1);
    }

    @Test
    public void testConcurrentInc() throws InterruptedException {
        long start = System.currentTimeMillis();
        int limit = 1_0000;
        Inc inc = new Inc();
        String key = "dist1:" + DateFormatUtils.format(new Date(), "yyyyMMddHHmmssSSS");
        DistributedLock lock = new RedisLock(key);
        System.out.println(key);
        redisTemplate.delete(key);
        int tn = 4;
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
