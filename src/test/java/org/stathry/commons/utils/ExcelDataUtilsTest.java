package org.stathry.commons.utils;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stathry.commons.pojo.config.ExcelRange;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * TODO
 *
 * @author stathry
 * @date 2018/4/13
 */
public class ExcelDataUtilsTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelDataUtilsTest.class);

    @Test
    public void testReadFromExcel() {
        List<Map<String, String>> list = ExcelDataUtils.readToMap("/temp/pullContactListSrc.xlsx");
        Assert.assertTrue(list != null && !list.isEmpty());
        Assert.assertEquals(62757, list.size());
        LOGGER.info("pullContactList size {}.", list.size());
        LOGGER.info("pullContactList first {}.", list.get(0));
        LOGGER.info("pullContactList last {}.", list.get(list.size()-1));
    }

    @Test
    public void testReadFromExcelWithRange1() {
        String path = "/temp/pullContactListSrc.xlsx";
//        List<String> keys = Arrays.asList("applyNo", "idName", "idNo");
        List<String> keys = Arrays.asList("applyNo");
        ExcelRange range = new ExcelRange(0, 1, 1, 11);
        List<Map<String, String>> list = ExcelDataUtils.readToMap(path, keys, range);
        Assert.assertTrue(list != null && !list.isEmpty());
        LOGGER.info("pullContactList first {}.", list.get(0));
        LOGGER.info("pullContactList last {}.", list.get(list.size()-1));
        Assert.assertEquals(10, list.size());
    }

    @Test
    public void testReadFromExcelWithRange2() {
        String path = "/temp/pullContactListSrc.xlsx";
//        List<String> keys = Arrays.asList("applyNo", "idName", "idNo");
        List<String> keys = Arrays.asList("applyNo");
        ExcelRange range = new ExcelRange(0, 1, 1, null);
        List<Map<String, String>> list = ExcelDataUtils.readToMap(path, keys, range);
        Assert.assertTrue(list != null && !list.isEmpty());
        LOGGER.info("pullContactList first {}.", list.get(0));
        LOGGER.info("pullContactList last {}.", list.get(list.size()-1));
        Assert.assertEquals(62757, list.size());
    }

}
