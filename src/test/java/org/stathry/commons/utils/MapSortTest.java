package org.stathry.commons.utils;

import com.alibaba.fastjson.JSON;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * MapSortTest
 * Created by dongdaiming on 2018-12-28 14:00
 */
public class MapSortTest {

    @Test
    public void testStringMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("k1", 1);
        map.put("k2", "v2");
        Assert.assertEquals("{\"k1\":1,\"k2\":\"v2\"}", JSON.toJSONString(map));
        Assert.assertEquals("{\"k1\":\"1\",\"k2\":\"v2\"}", JSON.toJSONString(MapSortUtils.toStringMap(map)));
        Assert.assertEquals("{\"k1\":\"1\",\"k2\":\"v2\"}", JSON.toJSONString(MapSortUtils.toNewStringMap(map)));
    }
}
