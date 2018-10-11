package org.stathry.commons.excel;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO
 * Created by dongdaiming on 2018-09-30 11:42
 */
public class ExcelWritingTest {

    @Test
    public void testWriteLinkedMaps1() {
        List<Map<String, Object>> maps = new ArrayList<>();
        LinkedHashMap<String, Object> map;
        double d = 800.889;
        for (int i = 0; i < 100; i++) {
            map = new LinkedHashMap<>(8);
            map.put("id", i);
            map.put("name", "name" + i);
            map.put("birth", DateUtils.addDays(new Date(), i));
            map.put("asserts", d + i);
            maps.add(map);
        }
        ExcelWriting.writeLinkedMaps("/temp/maps1.xlsx", maps);
    }

    @Test
    public void testWriteLinkedMaps2() {
        List<Map<String, Object>> maps = new ArrayList<>();
        LinkedHashMap<String, Object> map;
        double d = 800.889;
        for (int i = 0; i < 100; i++) {
            map = new LinkedHashMap<>(8);
            map.put("id", i);
            map.put("name", "name" + i);
            map.put("birth", DateUtils.addDays(new Date(), i));
            map.put("asserts", d + i);
            maps.add(map);
        }
        List<String> header = Arrays.asList("id", "name", "birth", "asserts");
        ExcelWriting.writeLinkedMaps("/temp/maps2.xlsx", maps, header);
    }

    @Test
    public void testWriteMaps3() {
        List<Map<String, Object>> maps = new ArrayList<>();
        Map<String, Object> map;
        double d = 800.889;
        for (int i = 0; i < 100; i++) {
            map = new HashMap<>(8);
            map.put("id", i);
            map.put("name", "name" + i);
            map.put("birth", DateUtils.addDays(new Date(), i));
            map.put("asserts", d + i);
            maps.add(map);
        }
        ExcelWriting.writeMaps("/temp/maps3.xlsx", maps, Arrays.asList("id", "name", "birth", "asserts"));
    }

    @Test
    public void testWriteMaps4() {
        List<Map<String, Object>> maps = new ArrayList<>();
        Map<String, Object> map;
        double d = 800.889;
        for (int i = 0; i < 100; i++) {
            map = new HashMap<>(8);
            map.put("id", i);
            map.put("name", "name" + i);
            map.put("birth", DateUtils.addDays(new Date(), i));
            map.put("asserts", d + i);
            maps.add(map);
        }
        List<String> keys = Arrays.asList("id", "name", "birth", "asserts");
        List<String> header = Arrays.asList("id", "姓名", "统计时间", "资产");
        ExcelWriting.writeMaps("/temp/maps4.xlsx", maps, keys, header);
    }

    @Test
    public void testWritingFromDB1() {

    }
}
