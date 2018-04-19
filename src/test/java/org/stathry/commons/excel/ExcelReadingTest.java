package org.stathry.commons.excel;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stathry.commons.enums.FileArea;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * TODO
 *
 * @author dongdaiming
 * @date 2018/4/19
 */
public class ExcelReadingTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelReadingTest.class);

    ExcelReadable excelReading = new ExcelReading();

    // method testReading100,time 1912
//    method testReading100VSAll,time 1590

    @Test
    public void testReading100() {
        long start = System.currentTimeMillis();
        String path = "/mnt/data/galaxy-pull/ssd-contact/悠融-闪信贷客户通讯录411/闪信贷待调取通讯录名单4.11.xlsx";
        List<String> keys = Arrays.asList("order", "name", "id");
        FileArea area = new FileArea(0, 3, 1, 101);
        List<Map<String, String>> list = excelReading.readToMaps(path, 0, keys, area);
        Assert.assertNotNull(list);
        Assert.assertTrue(!list.isEmpty());
        Assert.assertEquals(100, list.size());
        Assert.assertNotNull(list.get(0));
        Assert.assertEquals(3, list.get(0).size());
        LOGGER.info("first {}, last {}.", list.get(0), list.get(list.size() - 1));
        long end = System.currentTimeMillis();
        LOGGER.info("method {},time {}", Thread.currentThread().getStackTrace()[1].getMethodName(), end - start);
    }

    @Test
    public void testReading100VSAll() {
        long start = System.currentTimeMillis();
        String path = "/mnt/data/galaxy-pull/ssd-contact/悠融-闪信贷客户通讯录411/闪信贷待调取通讯录名单4.11.xlsx";
        List<String> keys = Arrays.asList("order", "name", "id");
        FileArea area = new FileArea(0, 3, 1, 500000);
        List<Map<String, String>> list = excelReading.readToMaps(path, 0, keys, area);
        Assert.assertNotNull(list);
        Assert.assertTrue(!list.isEmpty());
        LOGGER.info("first {}, last {}.", list.get(0), list.get(list.size() - 1));
        Assert.assertEquals(100, list.size());
        Assert.assertNotNull(list.get(0));
        Assert.assertEquals(3, list.get(0).size());
        long end = System.currentTimeMillis();
        LOGGER.info("method {},time {}", Thread.currentThread().getStackTrace()[1].getMethodName(), end - start);
    }

    @Test
    public void testReadingSheet2R100() {
        String path = "/mnt/data/galaxy-pull/ssd-contact/悠融-闪信贷客户通讯录411/闪信贷待调取通讯录名单4.11.xlsx";
        List<String> keys = Arrays.asList("order", "name", "id");
        FileArea area = new FileArea(0, 3, 1, 101);
        List<Map<String, String>> list = excelReading.readToMaps(path, 1, keys, area);
        Assert.assertNotNull(list);
        Assert.assertTrue(!list.isEmpty());
        Assert.assertEquals(100, list.size());
        Assert.assertNotNull(list.get(0));
        Assert.assertEquals(3, list.get(0).size());
        LOGGER.info("first {}, last {}.", list.get(0), list.get(list.size() - 1));
        Assert.assertEquals("邢书敏2", list.get(list.size() - 1).get("name"));
    }

    @Test
    public void testReading10() {
        String path = "/mnt/data/galaxy-pull/ssd-contact/悠融-闪信贷客户通讯录411/闪信贷待调取通讯录名单4.11.xlsx";
        List<String> keys = Arrays.asList("order", "name", "id");
        FileArea area = new FileArea(0, 3, 1, 11);
        List<Map<String, String>> list = excelReading.readToMaps(path, 0, keys, area);
        Assert.assertNotNull(list);
        Assert.assertTrue(!list.isEmpty());
        Assert.assertEquals(10, list.size());
        Assert.assertNotNull(list.get(0));
        LOGGER.info("first {}, last {}.", list.get(0), list.get(list.size() - 1));
        Assert.assertEquals(3, list.get(0).size());
    }

}
