package com.google.common.collect;

import com.alibaba.fastjson.JSON;
import org.apache.commons.collections.MultiHashMap;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Spliterator;
import java.util.TreeMap;

/**
 * CollectionTest
 * Created by dongdaiming on 2018-07-09 13:59
 */
public class CollectionTest {

    @Test
    public void testImmutableCollection() {
        List<String> list = ImmutableList.of("a", "b");
        System.out.println(list);

        List<String> list2 = ImmutableList.<String>builder().add("a1").add("b1").build();
        System.out.println(list2);

        Set<String> set = ImmutableSet.of("a", "b", "a");
        System.out.println(set);

        Map<String, String> map = ImmutableMap.of("k1", "v1", "k2", "v1");
        System.out.println(map);

        Exception e1 = null, e2 = null, e3 = null;

        try {
            list.add("x");
        }catch (Exception e) {
            e1 = e;
        }

        try {
            set.add("x");
        }catch (Exception e) {
            e2 = e;
        }

        try {
            map.put("kx", "vx");
        }catch (Exception e) {
            e3 = e;
        }

        Assert.assertNotNull(e1);
        Assert.assertNotNull(e2);
        Assert.assertNotNull(e3);
        System.out.println(e1.getClass() + ", " + e2.getClass() + ", " + e3.getClass());
    }

    @Test
    public void testMapCompare() {
        Map<Integer, Integer> map = new TreeMap<>();
        map.put(1, 11);
        map.put(2, 22);
        map.put(3, 33);
        map.put(4, 44);
        Map<Integer, Integer> map2 = new TreeMap<>();
        map2.put(1, 11);
        map2.put(2, 22);
        map2.put(33, 333);
        MapDifference<Integer, Integer> r = Maps.difference(map, map2);
        System.out.println("only left:" + JSON.toJSONString(r.entriesOnlyOnLeft()));
        System.out.println("only right:" + JSON.toJSONString(r.entriesOnlyOnRight()));
        System.out.println("commons:" + JSON.toJSONString(r.entriesInCommon()));
    }

    // BiMap k唯一且v唯一，如果v重复会抛异常
    @Test
    public void testBiMap() {
        BiMap<String, String> map = HashBiMap.create();
        map.put("k1", "v1");
        map.put("k1", "v2");
        System.out.println(map);
        Assert.assertEquals(1, map.size());

        BiMap<String, String> map2 = HashBiMap.create();
        map2.put("k1", "v1");
        try {
            map2.put("k2", "v1");
        } catch (Exception e) {
            Assert.assertTrue(e instanceof IllegalArgumentException);
            e.printStackTrace();
        }
        Assert.assertEquals(1, map2.size());

        Assert.assertEquals("k1", map.inverse().get("v2"));
    }

    // Multimap中一个key对应多个value
    @Test
    public void testMultiMap() {
        Multimap<String, String> map = HashMultimap.create();
        map.put("k1", "v11");
        map.put("k1", "v12");
        map.put("k2", "v21");
        map.put("k2", "v21");
        System.out.println(map);
        Assert.assertEquals(3, map.size());
        System.out.println(map.get("k1").getClass());
        Assert.assertTrue(map.get("k1") instanceof Set);
    }

    // Multiset中一个key对应多个value
    @Test
    public void testMultiSet() {
        Multiset<String> set = HashMultiset.create();
        set.add("k1");
        set.add("k1");
        set.add("k2");
        System.out.println(set);
        Assert.assertEquals(3, set.size());
        Assert.assertEquals(2, set.elementSet().size());
        Assert.assertEquals(2, set.count("k1"));
        Assert.assertTrue(set.contains("k1"));
        Assert.assertFalse(set.contains("k3"));
        System.out.println("elementSet:");
        for (String s : set.elementSet()) {
            System.out.println(s);
        }

        System.out.println("entrySet:");
        for (String s : set) {
            System.out.println(s);
        }
    }
}
