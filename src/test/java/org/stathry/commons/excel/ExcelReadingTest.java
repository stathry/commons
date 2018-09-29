package org.stathry.commons.excel;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stathry.commons.enums.FileArea;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * ExcelReadingTest
 *
 * @author dongdaiming
 * @date 2018/4/19
 */
public class ExcelReadingTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelReadingTest.class);

    @Test
    public void testReadToObject() {
        long start = System.currentTimeMillis();
        String path = "/temp/users.xlsx";
        List<User> list = ExcelReading.readToObjects(path, User.class, new FileArea(0, 4, 1, 10));
        System.out.println(JSON.toJSONString(list));
        Assert.assertNotNull(list);
        Assert.assertTrue(!list.isEmpty());
        Assert.assertEquals(10, list.size());
        Assert.assertNotNull(list.get(0));
        Assert.assertTrue(list.get(0).getClass() == User.class);
        LOGGER.info("first {}, last {}.", list.get(0), list.get(list.size() - 1));
        long end = System.currentTimeMillis();
        LOGGER.info("method {},time {}", Thread.currentThread().getStackTrace()[1].getMethodName(), end - start);
    }

    @Test
    public void testReadToMap() {
        long start = System.currentTimeMillis();
        String path = "/temp/users.xlsx";
        List<Map<String, String>> list = ExcelReading.readToMaps(path);
        System.out.println(JSON.toJSONString(list));
        Assert.assertNotNull(list);
        Assert.assertTrue(!list.isEmpty());
        Assert.assertEquals(17, list.size());
        Assert.assertNotNull(list.get(0));
        Assert.assertEquals(5, list.get(0).size());
        LOGGER.info("first {}, last {}.", list.get(0), list.get(list.size() - 1));
        long end = System.currentTimeMillis();
        LOGGER.info("method {},time {}", Thread.currentThread().getStackTrace()[1].getMethodName(), end - start);
    }

    @Test
    public void testReadToMapWithSheet() {
        long start = System.currentTimeMillis();
        String path = "/temp/users.xlsx";
        List<Map<String, String>> list = ExcelReading.readToMaps(path, null, null, "sheet2");
        System.out.println(JSON.toJSONString(list));
        Assert.assertNotNull(list);
        Assert.assertTrue(!list.isEmpty());
        Assert.assertEquals(17, list.size());
        Assert.assertNotNull(list.get(0));
        Assert.assertEquals(5, list.get(0).size());
        LOGGER.info("first {}, last {}.", list.get(0), list.get(list.size() - 1));
        long end = System.currentTimeMillis();
        LOGGER.info("method {},time {}", Thread.currentThread().getStackTrace()[1].getMethodName(), end - start);
    }

    @Test
    public void testReadToMapWithArea() {
        long start = System.currentTimeMillis();
        String path = "/temp/users.xlsx";
        List<String> keys = Arrays.asList("name", "age", "birth", "asserts");
        FileArea area = new FileArea(1, 4, 1, 10);
        List<Map<String, String>> list = ExcelReading.readToMaps(path, keys, area);
        System.out.println(JSON.toJSONString(list));
        Assert.assertNotNull(list);
        Assert.assertTrue(!list.isEmpty());
        Assert.assertEquals(area.getRowEnd() + 1 - area.getRowStart(), list.size());
        Assert.assertNotNull(list.get(0));
        Assert.assertEquals(keys.size(), list.get(0).size());
        LOGGER.info("first {}, last {}.", list.get(0), list.get(list.size() - 1));
        long end = System.currentTimeMillis();
        LOGGER.info("method {},time {}", Thread.currentThread().getStackTrace()[1].getMethodName(), end - start);
    }

    @Test
    public void testReadStr() {
        String path = "/temp/temp.xlsx";
        String content = ExcelReading.readToString(path);
        Assert.assertTrue(StringUtils.isNotBlank(content));
    }

    @Test
    public void testReadStrWithPwd() {
        String path = "/temp/temp2.xlsx";
        String content = ExcelReading.readToString(path, "666888");
        Assert.assertTrue(StringUtils.isNotBlank(content));
    }

    private static class User {
        private Long id;
        private Integer age;
        private Date birth;
        private Double asserts;

        @Override
        public String toString() {
            return "User{" +
                    "id=" + id +
                    ", age=" + age +
                    ", birth=" + birth +
                    ", asserts=" + asserts +
                    '}';
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        public Date getBirth() {
            return birth;
        }

        public void setBirth(Date birth) {
            this.birth = birth;
        }

        public Double getAsserts() {
            return asserts;
        }

        public void setAsserts(Double asserts) {
            this.asserts = asserts;
        }
    }

}
