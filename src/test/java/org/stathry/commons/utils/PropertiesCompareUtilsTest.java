package org.stathry.commons.utils;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import org.apache.commons.collections.MapUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.TreeMap;

/**
 * TODO
 *
 * @date 2018/3/14
 */
public class PropertiesCompareUtilsTest {

    public static boolean compareProperties(String path1, String path2) throws IOException {
        Properties prop1 = PropertiesLoaderUtils.loadProperties(new FileSystemResource(path1));
        Properties prop2 = PropertiesLoaderUtils.loadProperties(new FileSystemResource(path2));
        TreeMap<String, String> t1 = new TreeMap<String, String>((java.util.Map) prop1);
        TreeMap<String, String> t2 = new TreeMap<String, String>((java.util.Map) prop2);

        TreeMap<String, String> maxMap = t1.size() >= t2.size() ? t1 : t2;
        System.out.println("maxMap is " + (t1.size() >= t2.size() ? path1 : path2));
        System.out.println("size1=" + t1.size() + ",size2=" + t2.size());
        boolean same = false;
        boolean allSame = true;
        for (Map.Entry<String, String> e : maxMap.entrySet()) {
            same = e.getValue().equals(t2.get(e.getKey()));
            allSame = !same ? false : allSame;
            if (!same) {
                allSame = false;
                System.out.println("K=" + e.getKey() + ",same=" + same + ",maxV=" + e.getValue() + ",V=" + t2.get(e.getKey()));
            } else {
//            System.out.println("K=" + e.getKey() + ",same=" + same);
            }
        }
        allSame = allSame ? t1.size() == t2.size() : allSame;
        return allSame;
    }

    @Test
    public void testCompareProperties() throws IOException {
//        boolean allSame = compareProperties("/temp/galaxyETL-config-prod.properties", "/temp/galaxyETL-config-test.properties");
//        boolean allSame = compareProperties("/temp/510/galaxy-dev-config.properties", "/temp/510/galaxy-prd-config.properties");
        boolean allSame = compareProperties("/temp/816/config-test.properties", "/temp/816/config-prod.properties");
        System.out.println();
        System.out.println("allSame=" + allSame);
    }

    @Test
    public void testComparePropertiesKey() throws IOException {
        Properties prop1 = PropertiesLoaderUtils.loadProperties(new FileSystemResource("/temp/816/config-test.properties"));
        Properties prop2 = PropertiesLoaderUtils.loadProperties(new FileSystemResource("/temp/816/config-prod.properties"));
        MapDifference r = Maps.difference(prop1, prop2);
        System.out.println("only left:" + JSON.toJSONString(r.entriesOnlyOnLeft()));
        System.out.println("only right:" + JSON.toJSONString(r.entriesOnlyOnRight()));

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

}
