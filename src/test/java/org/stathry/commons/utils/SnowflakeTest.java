package org.stathry.commons.utils;

import com.google.common.math.IntMath;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.stathry.commons.dao.RedisManager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-context.xml")
public class SnowflakeTest {

    @Autowired
    private RedisManager redisManager;

    @Test
    public void testIncDataCenter() throws InterruptedException {
        final int n = 8;
        final int limit = 1000000;
        final Integer Z = 0;
        // dataCenterId可用于区分机器，workerId可用于区分业务
        final Snowflake snowflake1 = new Snowflake(redisManager.increment("order.inc") % 31, 1);
        final Snowflake snowflake2 = new Snowflake(redisManager.increment("order.inc") % 31, 1);
        final Map<String,Integer> all = new ConcurrentHashMap<>();
        ExecutorService exec = Executors.newFixedThreadPool(n);
        long start = System.currentTimeMillis();
        for (int i = 0; i < n; i++) {
            exec.submit(new Runnable() {
                @Override
                public void run() {
                    String o;
                    for(int j = 0; j < limit; j++) {
                        if(j % 2 == 0) {
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
        Snowflake snowflake = new Snowflake(1, 1);
        for (int i = 0; i < 10; i++) {
            System.out.println(snowflake.nextId());
        }
    }

    @Test
    public void testConcurrent() throws Exception {
        final int n = 8;
        final int limit = 1000000;
        final Integer Z = 0;
        // dataCenterId可用于区分机器，workerId可用于区分业务
        final Snowflake snowflake = new Snowflake(1, 1);
        final Map<String,Integer> all = new ConcurrentHashMap<>();
        ExecutorService exec = Executors.newFixedThreadPool(n);
        long start = System.currentTimeMillis();
        for (int i = 0; i < n; i++) {
            exec.submit(new Runnable() {
                @Override
                public void run() {
                        for(int j = 0; j < limit; j++) {
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
		for (int i = 0; i < ids.length; i ++) {
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
