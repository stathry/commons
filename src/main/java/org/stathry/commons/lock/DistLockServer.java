package org.stathry.commons.lock;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.stathry.commons.dao.RedisManager;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * DistLockServer
 * Created by dongdaiming on 2018-07-23 19:23
 */
public class DistLockServer {

    public static void main(String[] args) throws InterruptedException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring-context.xml");

        concurrentInc(context.getBean(RedisManager.class));
        	
        context.stop();
        context.close();
    }

    public static void concurrentInc(RedisManager redisManager) throws InterruptedException {
        long start = System.currentTimeMillis();
        int limit = 1_0000;
        String lk = "dist:lock:" + DateFormatUtils.format(new Date(), "yyyyMMddHH");
        String ik = "dist:inc:" + DateFormatUtils.format(new Date(), "yyyyMMddHH");
        DistributedLock lock = new RedisLock(lk);
        System.out.println(lk);
        int tn = 4;
        ExecutorService exec = Executors.newFixedThreadPool(tn);
        for (int i = 0; i < tn; i++) {
            exec.execute(() -> {
                for (int j = 0; j < limit; j++) {
                    try {
                        if (lock.lock()) {
                            redisManager.increment(ik);
                            lock.unlock();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        exec.shutdown();
        exec.awaitTermination(30, TimeUnit.MINUTES);
        System.out.println(redisManager.get(ik));
        System.out.println("times:" + tn * limit + ", sec:" + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - start));
    }

    public static void redissonConcurrentInc(RedisManager redisManager) throws InterruptedException {
        long start = System.currentTimeMillis();
        int limit = 1_0000;
        String lk = "dist:lock:" + DateFormatUtils.format(new Date(), "yyyyMMddHH");
        String ik = "dist:inc:" + DateFormatUtils.format(new Date(), "yyyyMMddHH");
        Lock lock = initSingleClient().getLock(lk);
        System.out.println(lk);
        int tn = 4;
        ExecutorService exec = Executors.newFixedThreadPool(tn);
        for (int i = 0; i < tn; i++) {
            exec.execute(() -> {
                for (int j = 0; j < limit; j++) {
                    lock.lock();
                    redisManager.increment(ik);
                    lock.unlock();
                }
            });
        }
        exec.shutdown();
        exec.awaitTermination(30, TimeUnit.MINUTES);
        System.out.println("times:" + tn * limit + ", sec:" + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - start));
    }

    private static RedissonClient initSingleClient() {
        Config conf = new Config();
        conf.useSingleServer().setAddress("redis://127.0.0.1:6379");
        return Redisson.create(conf);
    }

}
