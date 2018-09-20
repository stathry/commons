package org.stathry.commons.dao;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.stathry.commons.utils.ConfigManager;
import org.stathry.commons.utils.Snowflake;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * TODO
 *
 * @author stathry
 * @date 2018/4/2
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/spring-context.xml")
public class OrderNoTest {

    @Autowired
    private RedisManager redisManager;

    @Test
    public void testConcurrent() throws Exception {
        final int n = 8;
        final int limit = 100_0000;
        final Integer Z = 0;
        // dataCenterId可用于区分机器，workerId可用于区分业务
        long dataCenterId = redisManager.increment("order.host.flag") % 31;
        long workerId = ConfigManager.getSysObj("order.type.loan", Long.class) % 31;
        final Snowflake snowflake = new Snowflake(dataCenterId, workerId);
        final Map<String,Integer> all = new ConcurrentHashMap<>();
        ExecutorService exec = Executors.newFixedThreadPool(n);
        long start = System.currentTimeMillis();
        for (int i = 0; i < n; i++) {
            exec.submit(new Runnable() {
                @Override
                public void run() {
                    String o;
                    for(int j = 0; j < limit; j++) {
                        o = String.valueOf(snowflake.nextId());
                        all.put(o, Z);
                    }
                }
            });
        }
        exec.shutdown();
        exec.awaitTermination(5, TimeUnit.MINUTES);
        System.out.println(all.size());
        System.out.println("n=" + n + ",limit=" + limit + ",map size=" + all.size() + ",ms=" + (System.currentTimeMillis() - start));
        Assert.assertEquals(n * limit, all.size());
    }

}
