package org.stathry.commons.dao;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.stathry.commons.model.Actor;
import org.stathry.commons.model.City;

import java.util.List;

/**
 * TODO
 *
 * @author stathry
 * @date 2018/4/2
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/spring-context.xml")
public class ActorDAOTest {

    @Autowired
    private ActorDAO actorDAO;

    @Autowired
    private CityDAO cityDAO;

    @Test
    public void testQueryAll() {
        List<Actor> list = actorDAO.queryAll();
        System.out.println(list);
        System.out.println(list.size());
        Assert.assertTrue(list != null);
        Assert.assertTrue(list.size() > 0);
        Assert.assertTrue(list.size() == 200);
    }

    @Test
    public void testQueryByCityId() {
        System.out.println(cityDAO.queryById(6));
    }

    @Test
    public void testSaveCity() {
        City t = new City();
        t.setName("SUOMALI");
        t.setCountryCode("ABW");
        t.setDistrict("888");
        t.setPopulation(999);
        System.out.println(cityDAO.insert(t));
    }

}
