package org.apache.mybatis;

import org.apache.commons.lang3.RandomStringUtils;
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
import org.stathry.commons.dao.CountryDAO;
import org.stathry.commons.dao.impl.BatchDAOSkeleton;
import org.stathry.commons.model.City;
import org.stathry.commons.model.Country;

import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * TODO
 * Created by dongdaiming on 2018-11-09 15:42
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/spring-context.xml")
public class MybatisTest {

    @Autowired
    private CityDAO cityDAO;

    @Autowired
    private CountryDAO countryDAO;

    @Test
    public void testStartMybatisAndQuery() throws IOException {
        String resource = "mybatis/mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
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

    @Test
    public void testCache() throws IOException {

    }

    @Test
    public void testMultiEnvironment() throws IOException {
        String resource = "mybatis/mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream, "prd");
        Assert.assertNotNull(sqlSessionFactory);
    }

    @Test
    public void testInterfaceMapper() {
        Country country = countryDAO.queryById("CHN");
        System.out.println(country);
        Assert.assertNotNull(country);
    }

    @Test
    public void testUpdateByModel() {
        City city = new City();
        city.setId(4085);
        city.setPopulation(new Random().nextInt(1000));
        int n = cityDAO.updatePopulation(city);
        System.out.println(n);
        Assert.assertTrue(n > 0);
    }

    @Test
    public void testUpdateByFields() {
        int n2 = cityDAO.updatePopulation2(4084, (int)Math.random());
        System.out.println(n2);
        Assert.assertTrue(n2 > 0);
    }

    @Test
    public void testUpdateByMap1() {
        Map<String, Object> params = new HashMap<>(4);
        params.put("id", 4083);
        params.put("population", new Random().nextInt(1000));
        int n2 = cityDAO.updatePopulation3(params);
        System.out.println(n2);
        Assert.assertTrue(n2 > 0);
    }

    @Test
    public void testUpdateByMap2() {
        Map<String, Object> params = new HashMap<>(4);
        params.put("id", 4082);
        params.put("population", new Random().nextInt(1000));
        int n2 = cityDAO.updateByMap(params);
        System.out.println(n2);
        Assert.assertTrue(n2 > 0);
    }

    @Test
    public void testInsertAll() {
        insertAll(5, false);
    }

    private void insertAll(int limit, boolean batch) {
        List<City> list = new ArrayList<>(limit);
        City t;
        Random random = new SecureRandom();
        for (int i = 0; i < limit; i++) {
            t = new City();
            t.setName(RandomStringUtils.randomAlphabetic(4));
            t.setCountryCode("ZWE");
            t.setDistrict(RandomStringUtils.randomAlphanumeric(6));
            t.setPopulation(random.nextInt(1000));
            list.add(t);
        }

        int n = batch ? BatchDAOSkeleton.mybatisBatchInsert(list, 200, cityDAO) : cityDAO.insertAll(list);
        Assert.assertEquals(limit, n);
    }

    @Test
    public void testBatchInsert() {
        List<Integer> list = Arrays.asList(34, 100, 200, 201, 300, 400, 401);
        int c = 0;
        for (int i = 0, size = list.size(); i < size; i++) {
            insertAll(list.get(i), true);
            c += list.get(i);
        }
        System.out.println(c);
    }

}
