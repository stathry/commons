package org.apache.mybatis;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.decorators.SynchronizedCache;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.stathry.commons.mapper.CityMapper;
import org.stathry.commons.mapper.CountryMapper;
import org.stathry.commons.mapper.impl.BatchDAOSkeleton;
import org.stathry.commons.model.City;
import org.stathry.commons.model.Country;

import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * MybatisTest
 * Created by dongdaiming on 2018-11-09 15:42
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/spring-context.xml")
public class MybatisTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(MybatisTest.class);

    @Autowired
    private CityMapper cityMapper;

    @Autowired
    private CountryMapper countryMapper;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Test
    public void testStartMybatisAndQuery() throws IOException {
        String resource = "mybatis/mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession session = sqlSessionFactory.openSession();
        try {
            CityMapper mapper = session.getMapper(CityMapper.class);
            List<City> list = mapper.queryAll();
            System.out.println(list);
            City city = mapper.queryById(6);
            System.out.println(city);
        } finally {
            session.close();
        }
    }

    //    http://www.mybatis.org/mybatis-3/sqlmap-xml.html 搜索<cache
//    XMLMapperBuilder.cacheElement
    @Test
    public void testCache() throws IOException, InterruptedException, ParseException {
        Configuration cfg = sqlSessionFactory.getConfiguration();
        Assert.assertTrue(cfg.getCaches() != null && !cfg.getCaches().isEmpty());
        LOGGER.info("mybatis config, cacheEnabled {}, defaultStatementTimeout {}",
            cfg.isCacheEnabled(), cfg.getDefaultStatementTimeout());
        Assert.assertEquals(30, (int)cfg.getDefaultStatementTimeout());

        Object[] a = cfg.getCaches().toArray();
        Cache c = (Cache) a[0];

        int id = 15491;

        Timer timer = new Timer();
        Date queryTaskRunTime = DateUtils.truncate(new Date(), Calendar.MINUTE);
        queryTaskRunTime = DateUtils.addMinutes(queryTaskRunTime, 1);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                City city;
                    city = cityMapper.queryById(id);
                    Assert.assertNotNull(city);
                    LOGGER.info("cityId {}, population {}.", city.getId(), city.getPopulation());
                    LOGGER.info("cache size {}.", c.getSize());

            }
        }, queryTaskRunTime, 10000);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                LOGGER.info("will update city!");
                cityMapper.updatePopulation2(id, new Random().nextInt(1000));
            }
        }, DateUtils.addSeconds(queryTaskRunTime, 40));

        Thread.sleep(120000);

    }

    @Test
    public void testSessionCache() throws IOException, InterruptedException, ParseException {
        SqlSession session1 = sqlSessionFactory.openSession();
        SqlSession session2 = sqlSessionFactory.openSession();
        City city1 = session1.selectOne("org.stathry.commons.mapper.CityMapper.queryById", 15491);
        LOGGER.info("session1 query1, city1 {}", city1);
        City city2 = session1.selectOne("org.stathry.commons.mapper.CityMapper.queryById", 15491);
        LOGGER.info("session1 query2, city2 {}", city2);

        City city3 = session2.selectOne("org.stathry.commons.mapper.CityMapper.queryById", 15491);
        LOGGER.info("session2 query3, city3 {}", city3);
        City city4 = session2.selectOne("org.stathry.commons.mapper.CityMapper.queryById", 15491);
        LOGGER.info("session2 query4, city4 {}", city4);
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
        Country country = countryMapper.queryById("CHN");
        System.out.println(country);
        Assert.assertNotNull(country);
    }

    @Test
    public void testUpdateByModel() {
        City city = new City();
        city.setId(4085);
        city.setPopulation(new Random().nextInt(1000));
        int n = cityMapper.updatePopulation(city);
        System.out.println(n);
        Assert.assertTrue(n > 0);
    }

    @Test
    public void testUpdateByFields() {
        int n2 = cityMapper.updatePopulation2(4084, new Random().nextInt(1000));
        System.out.println(n2);
        Assert.assertTrue(n2 > 0);
    }

    @Test
    public void testUpdateByMap1() {
        Map<String, Object> params = new HashMap<>(4);
        params.put("id", 4083);
        params.put("population", new Random().nextInt(1000));
        int n2 = cityMapper.updatePopulation3(params);
        System.out.println(n2);
        Assert.assertTrue(n2 > 0);
    }

    @Test
    public void testUpdateByMap2() {
        Map<String, Object> params = new HashMap<>(4);
        params.put("id", 4082);
        params.put("population", new Random().nextInt(1000));
        int n2 = cityMapper.updateByMap(params);
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

//        int n = batch ? BatchDAOSkeleton.jdbcBatchInsert(null, list, 200, cityMapper) : cityMapper.insertAll(list);
//        Assert.assertEquals(limit, n);
    }

    @Test
    public void testBatchInsert() {
//        List<Integer> list = Arrays.asList(34, 100, 200, 201, 300, 400, 401);
        List<Integer> list = Arrays.asList( 201);
        int c = 0;
        for (int i = 0, size = list.size(); i < size; i++) {
            insertAll(list.get(i), true);
            c += list.get(i);
        }
        System.out.println(c);
    }

}
