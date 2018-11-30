package org.stathry.commons.dao;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.stathry.commons.mapper.CountryMapper;
import org.stathry.commons.model.Country;

import java.util.List;

/**
 * CityDAOTest
 *
 * @author stathry
 * @date 2018/4/2
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/spring-context.xml")
public class CountryDAOTest {

    @Autowired
    private CountryMapper countryDAO;

    @Test
    public void testQueryAll() {
        List<Country> list = countryDAO.queryAll();
        System.out.println(list);
        System.out.println(list.size());
        Assert.assertTrue(list != null);
        Assert.assertTrue(list.size() > 0);
    }

    @Test
    public void testQueryById() {
        Country country = countryDAO.queryById("CHN");
        System.out.println(country);
        Assert.assertNotNull(country);
    }

    @Test
    public void testSaveCity() {
        Country t = new Country();
        t.setCode("NAT");
        t.setCode2("NS");
        t.setName("Na Sao");
        t.setName("Na Sao");
        t.setContinent("Asia");
        t.setRegion("rg1");
        t.setSurfaceArea(6.6f);
        t.setPopulation(999);
        t.setLocalName("na na");
        t.setGovernmentForm("GCD");
        int n = countryDAO.insert(t);
        System.out.println(n);
        Assert.assertTrue(n > 0);
    }

}
