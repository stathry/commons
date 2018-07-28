package org.stathry.commons.redis;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.stathry.commons.dao.RedisManager;
import org.stathry.commons.pojo.Actor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * TODO
 * Created by dongdaiming on 2018-07-23 19:23
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-context.xml")
public class RedisManagerTest {

    @Autowired
    private RedisManager redisManager;

    @Test
    public void testSetGet1() {
        redisManager.set("k1", "v1", 30, TimeUnit.SECONDS);
        String v1 = (String) redisManager.get("k1");
        System.out.println(v1);
        Assert.assertEquals("v1", v1);

        redisManager.set("k2", "v2");
        List<String> list = redisManager.get(Arrays.asList("k1", "k2"));
        System.out.println(list);
        Assert.assertEquals(2, list.size());
    }

    @Test
    public void testSetGetString() {
        redisManager.setString("迪丽热巴", "迪丽热巴|迪力木拉提", 3, TimeUnit.MINUTES);
        String v2 = redisManager.getString("迪丽热巴");
        System.out.println(v2);
        Assert.assertEquals("迪丽热巴|迪力木拉提", v2);
    }

    @Test
    public void testSetGetInteger() {
        redisManager.set("lucky", 666888, 30, TimeUnit.SECONDS);
        int v1 = (int) redisManager.get("lucky");
        System.out.println(v1);
        Assert.assertEquals(666888, v1);
    }

    @Test
    public void testSetGetNormalJSONString() {
        Map<Integer, Actor> map = new HashMap<>(2);
        Actor actor1 = new Actor(666, "迪丽热巴", "迪力木拉提", new Date());
        Actor actor2 = new Actor(888, "懿", "司马", new Date());
        map.put(666, actor1);
        map.put(888, actor2);
        String json = JSON.toJSONStringWithDateFormat(map, "yyyy-MM-dd HH:mm:ss.SSS");
        redisManager.set("actMap", json);
        String va = (String) redisManager.get("actMap");
        System.out.println(va);
        Assert.assertEquals(json, va);
    }

    @Test
    public void testSetGetComplexJSONString() {
        Map<Integer, String> map2 = new HashMap<>(2);
        Actor actor3 = new Actor(6666, "迪丽热巴", "迪力木拉提", new Date());
        Actor actor4 = new Actor(888, "懿", "司马", new Date());
        map2.put(6666, JSON.toJSONStringWithDateFormat(actor3, "yyyy-MM-dd HH:mm:ss.SSS"));
        map2.put(8888, JSON.toJSONStringWithDateFormat(actor4, "yyyy-MM-dd HH:mm:ss.SSS"));
        String json2 = JSON.toJSONString(map2);
        redisManager.set("actMap2", json2);
        String va2 = (String) redisManager.get("actMap2");
        System.out.println(va2);
        Assert.assertEquals(json2, va2);
    }

    @Test
    public void testCHKey1() {
        Actor actor1 = new Actor(6666, "迪丽热巴", "迪力木拉提", new Date());
        redisManager.set("迪丽热巴", JSON.toJSONString(actor1));
        String v = (String) redisManager.get("迪丽热巴");
        System.out.println(v);
        Actor actor2 = JSON.parseObject(v, Actor.class);
        Assert.assertEquals(actor1, actor2);
    }

    @Test
    public void testCHKey2() {
        redisManager.getStringRedisTemplate().opsForValue().set("谷歌2", "硅谷2");
        String v2 = redisManager.getStringRedisTemplate().opsForValue().get("谷歌2");
        System.out.println(v2);
        Assert.assertEquals("硅谷2", v2);
    }

    @Test
    public void testExpire1() {
        redisManager.set("k2", "v2", 30, TimeUnit.SECONDS);
        String v2 = (String) redisManager.get("k2");
        System.out.println(v2);
        Assert.assertEquals("v2", v2);
        long k2Exp = redisManager.getExpire("k2");
        System.out.println(k2Exp);
        Assert.assertTrue(k2Exp >= 29);
    }

    @Test
    public void testExpire2() {
        redisManager.set("k3", "v3");
        redisManager.expire("k3", 30, TimeUnit.SECONDS);
        String v3 = (String) redisManager.get("k3");
        System.out.println(v3);
        Assert.assertEquals("v3", v3);
        long k3Exp = redisManager.getExpire("k3");
        System.out.println(k3Exp);
        Assert.assertTrue(k3Exp >= 29);
    }

    @Test
    public void testExpireAt() {
        redisManager.set("k4", "v4");
        redisManager.expireAt("k4", DateUtils.addSeconds(new Date(), 40));
        String v4 = (String) redisManager.get("k4");
        System.out.println(v4);
        Assert.assertEquals("v4", v4);
        long k4Exp = redisManager.getExpire("k4");
        System.out.println(k4Exp);
        Assert.assertTrue(k4Exp >= 38);
    }

    @Test
    public void testExists() {
        String key = UUID.randomUUID().toString();
        redisManager.set(key, UUID.randomUUID().toString());
        Assert.assertTrue(redisManager.exists(key));
        Assert.assertFalse(redisManager.exists( UUID.randomUUID().toString()));
    }

    @Test
    public void testDelete1() throws InterruptedException {
        String key = UUID.randomUUID().toString();
        System.out.println("testDelete:" + key);
        redisManager.set(key, UUID.randomUUID().toString());
        Assert.assertTrue(redisManager.exists(key));
        Thread.sleep(10000);
        redisManager.delete(key);
        Assert.assertFalse(redisManager.exists(key));
    }

    @Test
    public void testDeleteKeys() throws InterruptedException {
        String key = UUID.randomUUID().toString();
        System.out.println("testDeleteKeys:" + key);
        List<String> keys = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            keys.add(key + i);
        redisManager.set(key + i, i);
        }
        Assert.assertTrue(redisManager.exists(key + 0));

        Thread.sleep(10000);
        redisManager.delete(keys);
        Assert.assertFalse(redisManager.exists(key + 0));
    }

    @Test
    public void testInc() throws Exception {
        int tn = 8;
        ExecutorService exec = Executors.newFixedThreadPool(tn);
        int limit = 10_0000;
        String k = UUID.randomUUID().toString();
        System.out.println("inc key:" + k);
        for (int i = 0; i < tn; i++) {
            exec.execute(() -> {
                for (int j = 0; j < limit; j++) {
                    redisManager.increment(k);
                }
            });
        }
        exec.shutdown();
        exec.awaitTermination(3, TimeUnit.MINUTES);
        Long count = redisManager.increment(k);
        count = count - 1;
        System.out.println(count);
        Assert.assertEquals(tn * limit, count.longValue());
    }

    @Test
    public void testSetGetList() {
        List<Integer> list = Arrays.asList(6, 8, 666, 888, 666888);
        redisManager.setList("luckList", list, 30, TimeUnit.SECONDS);
        List<Integer> list2 = redisManager.getList("luckList");
        System.out.println(list2);
        Assert.assertTrue(list.equals(list2));
    }

    @Test
    public void testGetListByPattern() {
        redisManager.set("rule:p1", "rp1");
        redisManager.set("rule:p2", "rp2");
        List<String> list = redisManager.getListByPattern("rule*");
        System.out.println(list);
        Assert.assertEquals(2, list.size());
    }

    @Test
    public void testAddToListAndGet() {
        List<Integer> list = new ArrayList<>(Arrays.asList(6, 8, 666, 888, 666888));
        redisManager.setList("luckList1", list, 5, TimeUnit.MINUTES);
        List<Integer> list2 = redisManager.getList("luckList1");
        System.out.println(list2);
        Assert.assertTrue(list.equals(list2));

        redisManager.addToList("luckList1", 8866);
        list.add(8866);
        List<Integer> rl = redisManager.getList("luckList1");
        System.out.println(rl);
        Assert.assertTrue(list.equals(rl));
    }

    @Test
    public void testAddAllToList() {
        List<Integer> list = new ArrayList<>(Arrays.asList(6, 8, 666, 888, 666888));
        redisManager.setList("luckList", list, 30, TimeUnit.SECONDS);
        List<Integer> list2 = redisManager.getList("luckList");
        System.out.println(list2);
        Assert.assertTrue(list.equals(list2));

        redisManager.addAllToList("luckList", Arrays.asList(86, 8866));
        list.addAll(Arrays.asList(86, 8866));
        List<Integer> rl = redisManager.getList("luckList");
        System.out.println(rl);
        Assert.assertTrue(list.equals(rl));
    }

    @Test
    public void testConcurrentAddList() throws InterruptedException {
        int tn = 8;
        ExecutorService exec = Executors.newFixedThreadPool(tn);
        int limit = 10000;
        int unit = 10;
        String key = UUID.randomUUID().toString();
        System.out.println("list key:" + key);
        List<Long> list0 = new ArrayList<>(Arrays.asList(666L, 888L));
        redisManager.setList(key, list0, 8, TimeUnit.MINUTES);

        for (int i = 0; i < tn; i++) {
            exec.execute(() -> {
                List<Long> list;
                for (int j = 0; j < limit; j++) {
                        list = new ArrayList<>(unit);
                    for (int k = 0; k < unit; k++) {
                        list.add(System.nanoTime());
                    }
                    redisManager.addAllToList(key, list);
                }
            });
        }
        exec.shutdown();
        exec.awaitTermination(5, TimeUnit.MINUTES);

        List<Long> list1 = redisManager.getList(key);
        Assert.assertEquals((tn * limit * unit) + list0.size(),list1.size());
    }

    @Test
    public void testSetAndGetMap() {
        String k = String.valueOf(System.currentTimeMillis());
        Map<String, Integer> map = new HashMap<>(8);
        for (int i = 0; i < 10; i++) {
            map.put(k + i, i);
        }
        redisManager.setMap(k, map,3, TimeUnit.MINUTES);
        Map<String, Integer> map2 = redisManager.getMap(k);
        System.out.println(map2);
        Assert.assertTrue(map.equals(map2));
    }

    @Test
    public void testPutAndGetMap() {
        String k = String.valueOf(System.currentTimeMillis());
        Map<String, Integer> map = new HashMap<>(8);
        for (int i = 0; i < 10; i++) {
            map.put(k + i, i);
        }
        redisManager.setMap(k, map,3, TimeUnit.MINUTES);

        Map<String, Integer> mapT = new HashMap<>(4);
        mapT.put("k1", 11);
        mapT.put("k2", 22);
        map.putAll(mapT);
        redisManager.putToMap(k, mapT);

        Map<String, Integer> map2 = redisManager.getMap(k);
        System.out.println(map2);
        Assert.assertTrue(map.equals(map2));
    }

}
