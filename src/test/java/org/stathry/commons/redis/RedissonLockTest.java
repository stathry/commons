package org.stathry.commons.redis;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.TransportMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.stathry.commons.dao.RedisManager;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * TvODO
 * Created by dongdaiming on 2018-07-30 10:21
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-context.xml")
public class RedissonLockTest {

    @Autowired
    private RedisManager redisManager;

    //    https://github.com/redisson/redisson#quick-start
//    https://redisson.org/
//    https://github.com/redisson/redisson/issues/967
//    https://github.com/redisson/redisson/wiki/8.-distributed-locks-and-synchronizers#81-lock
    @Test
    public void testInitClusterClient() {
        Config conf = new Config();
        conf.setTransportMode(TransportMode.EPOLL);
        conf.useClusterServers().addNodeAddress("redis://127.0.0.1:6379", "redis://127.0.0.1:6379");
        RedissonClient redisson = Redisson.create(conf);
        System.out.println(redisson);
    }

    @Test
    public void testInitSingleClient() {
        RedissonClient rc = initSingleClient();
        Assert.assertNotNull(rc);
        System.out.println(rc);
    }

    private RedissonClient initSingleClient() {
        Config conf = new Config();
        conf.useSingleServer().setAddress("redis://127.0.0.1:6379");
        return Redisson.create(conf);
    }

    @Test
    public void testLock() throws InterruptedException {
        RedissonClient rc = initSingleClient();
        String lk = UUID.randomUUID().toString();
        String ik = UUID.randomUUID().toString();
        System.out.println("lockKey:" + lk);
        System.out.println("incKey:" + ik);

        RLock lock = rc.getLock(lk);
        int tn = 4;
        int limit = 10_0000;
        Counter c = new Counter();
        ExecutorService exec = Executors.newFixedThreadPool(tn);
        for (int i = 0; i < tn; i++) {
            exec.execute(() -> {
                for (int j = 0; j < limit; j++) {
                    lock.lock();
                    c.count();
                    lock.unlock();
                }
            });
        }
        exec.shutdown();
        exec.awaitTermination(5, TimeUnit.MINUTES);
        long inc = c.getC();
        System.out.println(inc);
        Assert.assertEquals(tn * (long) limit, (long) inc);
    }

    private static class Counter {

        private long c;

        public long count() {
            c += 1L;
            return c;
        }

        public long getC() {
            return c;
        }
    }

}
