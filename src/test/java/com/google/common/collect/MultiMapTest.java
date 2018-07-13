package com.google.common.collect;

import org.apache.commons.collections.MultiHashMap;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * TODO
 * Created by dongdaiming on 2018-07-09 13:59
 */
public class MultiMapTest {

    @Test
    public void testMultiMapTest() {
        Multimap<String, String> map = ArrayListMultimap.create();
        map.put("girl", "dwj");
        map.put("girl", "wqf");
        map.put("wife", "smart");
        System.out.println(map);
        Assert.assertEquals(3, map.size());
        Collection<String> girls = map.get("girl");
        System.out.println(girls);
        Assert.assertEquals(2, girls.size());
    }
}
