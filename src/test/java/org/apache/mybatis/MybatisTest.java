package org.apache.mybatis;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.stathry.commons.dao.CityDAO;
import org.stathry.commons.dao.RedisManager;
import org.stathry.commons.model.City;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * TODO
 * Created by dongdaiming on 2018-11-09 15:42
 */

public class MybatisTest {

    @Test
    public void testStartMybatis() throws IOException {
        String resource = "mybatis/mybatis-config.xml";
//        String resource = "spring-context.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        //从 XML 中构建 SqlSessionFactory
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession session = sqlSessionFactory.openSession();
        try {
            CityDAO mapper = session.getMapper(CityDAO.class);
            List<City> list = mapper.queryAll();
            System.out.println(list);
            City city = mapper.queryById(6);
            System.out.println(city);
        } finally {
            session.close();
        }

    }

}
