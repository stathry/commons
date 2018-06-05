package org.stathry.commons.jdbc;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.*;

/**
 * TODO
 * Created by dongdaiming on 2018-06-05 14:20
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-context.xml")
public class JDBCTest {

    @Autowired
    private DataSource dataSource;

    @Test
    public void testConnPoolOneClose() throws SQLException, InterruptedException {
        Connection conn1 = dataSource.getConnection();
        String s1 = conn1.toString();
        System.out.println(conn1);
        conn1.close();
        Thread.sleep(1000);
        Connection conn2 = dataSource.getConnection();
        String s2 = conn2.toString();
        System.out.println(conn2);
        System.out.println("s1:" + s1 + ",s2:" + s2 + ", s1==s2?" + (s1.equals(s2)));
        assertEquals(s1, s2);
    }

    @Test
    public void testConnPoolTwo() throws SQLException, InterruptedException {
        Connection conn1 = dataSource.getConnection();
        String s1 = conn1.toString();
        System.out.println(conn1);
        Thread.sleep(1000);
        Connection conn2 = dataSource.getConnection();
        String s2 = conn2.toString();
        System.out.println(conn2);
        System.out.println("s1:" + s1 + ",s2:" + s2 + ", s1==s2?" + (s1.equals(s2)));
        assertNotEquals(s1, s2);
    }


}
