package org.stathry.commons.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.stathry.commons.utils.DBUtils;

import java.util.ArrayList;
import java.util.List;

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
}
