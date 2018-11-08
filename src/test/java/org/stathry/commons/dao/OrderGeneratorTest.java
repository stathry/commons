package org.stathry.commons.dao;

import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.stathry.commons.utils.ConfigManager;
import org.stathry.commons.utils.OrderGenerator;
import org.stathry.commons.utils.Snowflake;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 分布式订单生成测试
 *
 * @author stathry
 * @date 2018/4/2
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = "classpath:/spring-context.xml")
public class OrderGeneratorTest {

    @Autowired
    private RedisManager redisManager;
    @Autowired
    private OrderService orderService;

    @Test
    public void testMongoOrder() {
        for (int i = 0; i < 10; i++) {
            System.out.println(ObjectId.get().toString());
        }
    }

    @Test
    public void testMongoOrderTimeSingle() {
        final int orderLen = 30;
        // 单线程测试
        int limit = 100_0000;
        Set<String> set = new HashSet<>(limit * 2);
        long start = System.currentTimeMillis();
        for (int i = 0; i < limit; i++) {
            set.add(ObjectId.get().toString());
//            generator.order();
        }
        System.out.println("mongo gen order:" + limit + ", time:" + (System.currentTimeMillis() - start));
        Assert.assertEquals(limit, set.size());
    }

    @Test
    public void testSnowOrder() {
        Snowflake snowflake = new Snowflake(1, 1);
        for (int i = 0; i < 10; i++) {
            System.out.println(snowflake.nextId());
        }
    }

    @Test
    public void testSnowOrderTimeSingle() {
        // 单线程测试
        int limit = 100_0000;
        Set<String> set = new HashSet<>(limit * 2);
        Snowflake snowflake = new Snowflake(1, 1);
        long start = System.currentTimeMillis();
        for (int i = 0; i < limit; i++) {
            set.add(String.valueOf(snowflake.nextId()));
//            generator.order();
        }
        System.out.println("snow gen order:" + limit + ", time:" + (System.currentTimeMillis() - start));
        Assert.assertEquals(limit, set.size());
    }

    @Test
    public void testOrderService1() {
        for (int i = 0; i < 10; i++) {
            System.out.println(orderService.order());
        }
    }

    @Test
    public void testSnowConcurrent() throws Exception {
        final int n = 4;
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

    @Test
    public void testGenOrder() {
        final int orderLen = 30;
        // 输出样例
        OrderGenerator generator = new OrderGenerator(orderLen);
        for (int i = 0; i < 10; i++) {
            System.out.println(generator.order());
        }
    }

    @Test
    public void testGenOrderTimeSingle() {
        final int orderLen = 30;
        // 单线程测试
        int limit = 100_0000;
        OrderGenerator generator = new OrderGenerator(orderLen);
        Set<String> set = new HashSet<>(limit * 2);
        long start = System.currentTimeMillis();
        for (int i = 0; i < limit; i++) {
            set.add(generator.order());
//            generator.order();
        }
        System.out.println("gen order:" + limit + ", time:" + (System.currentTimeMillis() - start));
        Assert.assertEquals(limit, set.size());
    }

    @Test
    public void testGenOrderMultiThread() throws InterruptedException {
        final int orderLen = 30;
        // 并发测试
        int tn = 4;
        final int limit = 100_0000;
        ExecutorService exec = Executors.newFixedThreadPool(tn);
        final ConcurrentHashMap<String, Integer> set = new ConcurrentHashMap<>(limit * tn * 2);
        long start = System.currentTimeMillis();
        for (int i = 0; i < tn; i++) {
            exec.execute(new Runnable() {
                @Override
                public void run() {
                    OrderGenerator generator = new OrderGenerator(orderLen);
                    for (int i = 0; i < limit; i++) {
                        set.put(generator.order(), 1);
                    }
                }
            });
        }
        exec.shutdown();
        exec.awaitTermination(5, TimeUnit.MINUTES);
        System.out.println(set.size());
        System.out.println("gen order:" + tn * limit + ", time:" + (System.currentTimeMillis() - start));
        Assert.assertEquals(tn * limit, set.size());
    }

}
