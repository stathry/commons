package org.stathry.commons;

import com.alibaba.druid.pool.DruidDataSource;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.stathry.commons.utils.ApplicationContextUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * TODO
 * Created by dongdaiming on 2018-06-05 14:20
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-context.xml")
public class SpringTest {

    @Test
    public void testStart() {
        System.out.println("spring started.");
    }

    @Test
    public void testGetBeanByNameAndType() {
        DruidDataSource bean = ApplicationContextUtils.getBean("dataSource", DruidDataSource.class);
        System.out.println(bean);
        Assert.assertNotNull(bean);
    }

}
