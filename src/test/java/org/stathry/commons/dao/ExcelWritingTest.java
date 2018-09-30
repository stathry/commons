package org.stathry.commons.dao;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;
import org.stathry.commons.excel.ExcelWriting;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * TODO
 * Created by dongdaiming on 2018-09-30 11:42
 */
public class ExcelWritingTest {

    @Test
    public void testWriteLinkedMaps1() {
        List<LinkedHashMap<String, Object>> maps = new ArrayList<>();
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
}
