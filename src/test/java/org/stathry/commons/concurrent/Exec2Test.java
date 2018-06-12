package org.stathry.commons.concurrent;

import org.junit.Test;
import org.stathry.commons.utils.Executors2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * TODO
 * Created by dongdaiming on 2018-06-05 17:06
 */
public class Exec2Test {

    @Test
    public void testExecutorsFix() throws InterruptedException {
        ExecutorService exec = Executors.newFixedThreadPool(1);
        exec.submit(() -> {
            System.out.println(Thread.currentThread().getName());
        });
        exec.shutdown();
        exec.awaitTermination(1, TimeUnit.MINUTES);
    }

    @Test
    public void testExecThreadName() throws InterruptedException {
        int tasks = 10_0000;
        ExecutorService exec1 = Executors2.newDefaultExecutorService("biz1-");
        ExecutorService exec2 = Executors2.newDefaultExecutorService("biz2-");
        for (int i = 0; i < tasks; i++) {
            final int index = i;
            if(index % 2 == 0) {
            exec2.submit(() -> {
                System.out.println("i:" + index + ", " + Thread.currentThread().getName());
            });
            } else {
                exec1.submit(() -> {
                    System.out.println("i:" + index + ", " + Thread.currentThread().getName());
                });
            }
        }
        exec1.shutdown();
        exec2.shutdown();
        exec1.awaitTermination(3, TimeUnit.MINUTES);
        exec2.awaitTermination(3, TimeUnit.MINUTES);
    }

    @Test
    public void testExecThreadNamePoolSize() throws InterruptedException {
        int tasks = 10_0000;
        ExecutorService exec = Executors2.newDefaultExecutorService("biz3-", 8, 32);
        for (int i = 0; i < tasks; i++) {
            final int index = i;
            exec.submit(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("i:" + index + ", " + Thread.currentThread().getName());
            });
        }
        exec.shutdown();
        exec.awaitTermination(3, TimeUnit.MINUTES);
    }

    @Test
    public void testExecReject() throws InterruptedException {
        int tasks = 10_0000;
        ExecutorService exec = Executors2.newDefaultExecutorService();
        for (int i = 0; i < tasks; i++) {
            final int index = i;
            exec.submit(() -> {
                System.out.println("i:" + index + ", " + Thread.currentThread().getName());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        exec.shutdown();
        exec.awaitTermination(3, TimeUnit.MINUTES);
    }

    @Test
    public void testExecReject2() throws InterruptedException {
        int tasks = 10_0000;
        ExecutorService exec = Executors2.newDefaultExecutorService();
        for (int i = 0; i < tasks; i++) {
            final int index = i;
            // execute与submit的区别是submit会将任务包装成FutureTask然后再调用execute最后返回FutureTask,execute不会把任务包装成FutureTask，也没有返回值
            exec.execute(new CustTask1());
        }
        exec.shutdown();
        exec.awaitTermination(3, TimeUnit.MINUTES);
    }

    private static class CustTask1 implements Runnable {

        @Override
        public void run() {
            System.out.println("run custTask1.");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
