package org.stathry.commons.dao;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.stathry.commons.pojo.dto.TableArea;
import org.stathry.commons.utils.DBUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO
 * Created by dongdaiming on 2018-08-15 09:40
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/spring-context.xml")
public class DBUtilsTest {

    @Test
    public void testBatchInsertFromExcel() {
        DBUtils.insertFromExcel("/temp/users2.xlsx", "users");
    }

    @Test
    public void testExportToExcel1() throws IOException {
        String path = File.createTempFile("users-", ".xlsx", new File("/temp")).getPath();
        int n = DBUtils.exportToExcel(path, "users", new TableArea<Integer>());
        System.out.println(n);
        Assert.assertTrue(n > 0);
    }

    @Test
    public void testListDataFromDB1() {
        List<Map<String, Object>> list = DBUtils.listData("users", new TableArea<Integer>());
        System.out.println(JSON.toJSONString(list));

        list = DBUtils.listData("users", new TableArea<Integer>("id", 5, 10));
        System.out.println("1:" + JSON.toJSONString(list));

        list = DBUtils.listData("users", new TableArea<Integer>("id", 5, null));
        System.out.println("2:" + JSON.toJSONString(list));

        list = DBUtils.listData("users", new TableArea<Integer>("id", null, 10));
        System.out.println("3:" + JSON.toJSONString(list));

        list = DBUtils.listData("users", new TableArea<Integer>("birth",
                DateUtils.addDays(new Date(), 1), DateUtils.addDays(new Date(), 3)));
        System.out.println("4:" + JSON.toJSONString(list));

        list = DBUtils.listData("users", new TableArea<Integer>("birth",
                DateUtils.addDays(new Date(), 1), null));
        System.out.println("5:" + JSON.toJSONString(list));

        list = DBUtils.listData("users", new TableArea<Integer>("birth",
                null, DateUtils.addDays(new Date(), 3)));
        System.out.println("6:" + JSON.toJSONString(list));

    }
}
