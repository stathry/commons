package org.stathry.commons.redis;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.stathry.commons.bean.RedisManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * TODO
 * Created by dongdaiming on 2018-11-09 15:42
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-context.xml")
public class DuplicateSubmissionTest {

    @Autowired
    private RedisManager redisManager;

    private AtomicLong c = new AtomicLong();

    @Test
    public void testSingleOneSub() {
        int tn = 3;
        int limit = 1000;
        for (int i = 0; i < tn; i++) {
            for (int j = 0; j < limit; j++) {
                if (redisManager.setNX("user:" + j)) {
                    c.incrementAndGet();
                }
            }
        }
        Assert.assertEquals((long) limit, c.get());
    }

    @Test
    public void testSingleManySub() {
    }

    @Test
    public void testMultiThreadSub() throws InterruptedException {
        int tn = 4;
        int limit = 10000;
        ExecutorService exec = Executors.newFixedThreadPool(tn);
        for (int i = 0; i < tn; i++) {
            exec.execute(() -> {
                for (int j = 0; j < limit; j++) {
                    if (redisManager.setNX("user2:" + j)) {
                        c.incrementAndGet();
                    }
                }
            });
        }
        exec.shutdown();
        exec.awaitTermination(3, TimeUnit.MINUTES);
        Assert.assertEquals((long) limit, c.get());
    }
}
