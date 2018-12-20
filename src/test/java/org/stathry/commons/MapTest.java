package org.stathry.commons;

import com.alibaba.fastjson.JSON;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.util.LinkedCaseInsensitiveMap;

import java.util.Map;

/**
 * MapTest
 * Created by dongdaiming on 2018-12-14 14:23
 */
public class MapTest {

    // LinkedCaseInsensitiveMap的key不区分大小写
    @Test
    public void testLinkedCaseInsensitiveMap() {
        Map<String, Integer> map = new LinkedCaseInsensitiveMap<>();
        map.put("key1", 1);
        map.put("KEY1", 2);
        System.out.println(map);
        Assert.assertEquals(1, map.size());
        Assert.assertEquals(2, map.get("key1").intValue());
        Assert.assertEquals(2, map.get("Key1").intValue());
        Assert.assertEquals(2, map.get("KEY1").intValue());
    }

    @Test
    public void testMapKVTypeNotMatch() {
        Map<String,String> map = JSON.parseObject("{'k1':'v1', 'k2':2}", Map.class);
        System.out.println(map);
        System.out.println(JSON.toJSONString(map));
        Assert.assertTrue((Object)map.get("k2") instanceof Integer);
    }

}
