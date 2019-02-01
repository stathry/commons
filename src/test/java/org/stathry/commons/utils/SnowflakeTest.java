package org.stathry.commons.utils;

import com.google.common.math.IntMath;
import com.google.common.math.LongMath;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.stathry.commons.bean.RedisManager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration("classpath:spring-context.xml")
public class SnowflakeTest {

    @Autowired
    private RedisManager redisManager;

    @Test
    public void testLeftShiftRange() {
        System.out.println(Long.MAX_VALUE);
        long timestamp = DatetimeUtils.parseQuietly("2210-01-01", "yyyy-MM-dd").getTime();
        long epoch = DatetimeUtils.parseQuietly("2010-01-01", "yyyy-MM-dd").getTime();
        long interval = timestamp - epoch;
        System.out.println("interval:" + interval);
        System.out.println(3155673600000L << 20L);
        System.out.println(3155673600000L << 20L);
    }

    @Test
    public void testLeftShift() {
        final long timestampBits = 41L;
        final long datacenterIdBits = 4L;
        final long workerIdBits = 4L;
        final long sequenceBits = 12L;

        final long timestampShift = sequenceBits + datacenterIdBits + workerIdBits;
        final long datacenterIdShift = sequenceBits + workerIdBits;
        final long workerIdShift = sequenceBits;

        long timestamp = DatetimeUtils.parseQuietly("2110-01-01", "yyyy-MM-dd").getTime();
//        long timestamp = DatetimeUtils.parseQuietly("2019-01-01", "yyyy-MM-dd").getTime();
        long epoch = DatetimeUtils.parseQuietly("2010-01-01", "yyyy-MM-dd").getTime();
        long datacenterId = 1L;
        long workerId = 2L;
        long sequence = 0;

        long result = ((timestamp - epoch) << timestampShift) | //
                (datacenterId << datacenterIdShift) | //
                (workerId << workerIdShift) | // new line for nice looking
                sequence;
        System.out.println("timestamp - epoch=" + (timestamp - epoch));
        System.out.println("timestampShift=" + timestampShift);
        System.out.println("(timestamp - epoch) << timestampShift=" + ((timestamp - epoch) << timestampShift));
        System.out.println(result);
    }

    @Test
    public void testExtSnowflake() {
        // 可调整各段所占比特位位数，或在尾端追加递增序列来进行扩展
        final long sequenceBits = 20L;
        final long maxSequence = -1L ^ (-1L << sequenceBits); // 2^12-1
        System.out.println(maxSequence);

        Snowflake snowflake1 = new Snowflake(1, 1);
        Snowflake2 snowflake2 = new Snowflake2(1, 1);
        System.out.println(snowflake1.nextId());
        System.out.println();

        for (int i = 0; i < 10; i++) {
            System.out.println(snowflake2.nextId());
        }
    }

    @Test
    public void testMask() {
        long seqMask1 = LongMath.pow(2, 12) - 1; //计算12位能耐存储的最大正整数，相当于：2^12-1 = 4095
        System.out.println("seqMask1: "+seqMask1);

        long seqMask = -1L ^ (-1L << 12L); //计算12位能耐存储的最大正整数，相当于：2^12-1 = 4095
        System.out.println("seqMask: "+seqMask);
        System.out.println(1L & seqMask);
        System.out.println(2L & seqMask);
        System.out.println(3L & seqMask);
        System.out.println(4L & seqMask);
        System.out.println(4095L & seqMask);
        System.out.println(4096L & seqMask);
        System.out.println(4097L & seqMask);
        System.out.println(4098L & seqMask);
    }

    @Test
    public void testMaxIntUseBits() {
        System.out.println(IntMath.pow(2, 5) - 1);
        System.out.println(IntMath.pow(2, 12) - 1);
    }

    @Test
    public void testIncDataCenter() throws InterruptedException {
        final int n = 8;
        final int limit = 1000000;
        final Integer Z = 0;
        // dataCenterId可用于区分机器，workerId可用于区分业务
        final Snowflake snowflake1 = new Snowflake(redisManager.increment("order.inc") % 31, 1);
        final Snowflake snowflake2 = new Snowflake(redisManager.increment("order.inc") % 31, 1);
        final Map<String, Integer> all = new ConcurrentHashMap<>();
        ExecutorService exec = Executors.newFixedThreadPool(n);
        long start = System.currentTimeMillis();
        for (int i = 0; i < n; i++) {
            exec.submit(new Runnable() {
                @Override
                public void run() {
                    String o;
                    for (int j = 0; j < limit; j++) {
                        if (j % 2 == 0) {
                            o = String.valueOf(snowflake1.nextId());
                        } else {
                            o = String.valueOf(snowflake2.nextId());
                        }
//                System.out.println(o);
                        all.put(o, Z);
                    }
                }
            });
        }
        exec.shutdown();
        exec.awaitTermination(30, TimeUnit.SECONDS);
        System.out.println(all.size());
        Assert.assertEquals(n * limit, all.size());
        System.out.println("n=" + n + ",limit=" + limit + ",map size=" + all.size() + ",ms=" + (System.currentTimeMillis() - start));
    }

    @Test
    public void testHashcode1() {
        Assert.assertEquals(31, IntMath.pow(2, 5) - 1);
        Assert.assertEquals(-1, -1 % 31);
        Assert.assertEquals(0, -31 % 31);
        Assert.assertEquals(-1, -32 % 31);
    }

    @Test
    public void testSnowflake1() {
        Snowflake snowflake = new Snowflake(1, 1);
        String s = String.valueOf(snowflake.nextId());
        Assert.assertEquals(18, s.length());
        System.out.println(s);
    }

    @Test
    public void testSnowflakes() {
        int limit = 100_0000;
        Snowflake snowflake = new Snowflake(1, 1);
        long start = System.currentTimeMillis();
        for (int i = 0; i < limit; i++) {
            snowflake.nextId();
        }
        System.out.println("gen order:" + limit + ", time:" + (System.currentTimeMillis() - start));
    }

    @Test
    public void testConcurrent() throws Exception {
        final int n = 8;
        final int limit = 1000000;
        final Integer Z = 0;
        // dataCenterId可用于区分机器，workerId可用于区分业务
        final Snowflake snowflake = new Snowflake(1, 1);
        final Map<String, Integer> all = new ConcurrentHashMap<>();
        ExecutorService exec = Executors.newFixedThreadPool(n);
        long start = System.currentTimeMillis();
        for (int i = 0; i < n; i++) {
            exec.submit(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < limit; j++) {
                        String o = String.valueOf(snowflake.nextId());
//                System.out.println(o);
                        all.put(o, Z);
                    }
                }
            });
        }
        exec.shutdown();
        exec.awaitTermination(1, TimeUnit.SECONDS);
        System.out.println(all.size());
        System.out.println("n=" + n + ",limit=" + limit + ",map size=" + all.size() + ",ms=" + (System.currentTimeMillis() - start));
    }

    @Test
    public void testNextIdAndParse() throws Exception {
        long beginTimeStamp = System.currentTimeMillis();
        Snowflake snowflake = new Snowflake(3, 16);

        // gen id and parse it
        long id = snowflake.nextId();
        long[] arr = snowflake.parseId(id);
        System.out.println(snowflake.formatId(id));

        Assert.assertTrue(arr[0] >= beginTimeStamp);
        Assert.assertEquals(3, arr[1]); // datacenterId
        Assert.assertEquals(16, arr[2]); // workerId
        Assert.assertEquals(0, arr[3]); // sequenceId

        // gen two ids in different MS
        long id2 = snowflake.nextId();
        Assert.assertFalse(id == id2);
        System.out.println(snowflake.formatId(id2));

        Thread.sleep(1); // wait one ms
        long id3 = snowflake.nextId();
        long[] arr3 = snowflake.parseId(id3);
        System.out.println(snowflake.formatId(id3));
        Assert.assertTrue(arr3[0] > arr[0]);

        // gen two ids in the same MS
        long[] ids = new long[2];
        for (int i = 0; i < ids.length; i++) {
            ids[i] = snowflake.nextId();
        }
        System.out.println(snowflake.formatId(ids[0]));
        System.out.println(snowflake.formatId(ids[1]));
        Assert.assertFalse(ids[0] == ids[1]);
        long[] arr_ids0 = snowflake.parseId(ids[0]);
        long[] arr_ids1 = snowflake.parseId(ids[1]);
        Assert.assertEquals(arr_ids0[0], arr_ids1[0]);
        Assert.assertEquals(arr_ids0[3], arr_ids1[3] - 1);
    }

}
