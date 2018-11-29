package org.stathry.commons.dao;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.stathry.commons.model.City;

import java.util.List;

/**
 * CityDAOTest
 *
 * @author stathry
 * @date 2018/4/2
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/spring-context.xml")
public class CityDAOTest {

    @Autowired
    private CityDAO cityDAO;

    @Test
    public void testQueryAll() {
        List<City> list = cityDAO.queryAll();
        System.out.println(list);
        System.out.println(list.size());
        Assert.assertTrue(list != null);
        Assert.assertTrue(list.size() > 0);
    }

    @Test
    public void testQueryByCityId() {
        City city = cityDAO.queryById(4079);
        System.out.println(city);
        Assert.assertNotNull(city);
    }

    @Test
    public void testSaveCity() {
        City t = new City();
        t.setName("NASAO");
        t.setCountryCode("ABW");
        t.setDistrict("666");
        t.setPopulation(999);
        int n = cityDAO.insert(t);
        System.out.println(n);
        Assert.assertTrue(n > 0);
    }

}
