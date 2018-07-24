package org.stathry.commons.dao;

import org.apache.commons.lang3.RandomStringUtils;
import static org.junit.Assert.*;

import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.stathry.commons.pojo.Actor;
import org.stathry.commons.pojo.Actor2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * TODO
 * Created by dongdaiming on 2018-07-24 17:45
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-context.xml")
public class MongoTest {

    @Autowired
    private MongoManager mongoManager;

    @Test
    public void testSaveDocByMap() {
        Map<String, Object> doc = new HashMap<>(8);
        doc.put("actorId", new Random().nextInt(10000));
        doc.put("firstName", RandomStringUtils.random(3, "迪丽热巴".toCharArray()));
        doc.put("lastName", RandomStringUtils.random(3, "GOOGLE".toCharArray()));
        doc.put("lastUpdate", new Date());
        mongoManager.saveDoc("actor", doc);
        System.out.println(doc);
    }

//    @Test
    public void testBatchSaveDocByMap() {
        List<Map<String, Object>> list = new ArrayList<>(5);
        Map<String, Object> doc;
        for (int i = 0; i < 5; i++) {
            doc = new HashMap<>(8);
        doc.put("actorId", new Random().nextInt(10000));
        doc.put("firstName", RandomStringUtils.random(3, "迪丽热巴".toCharArray()));
        doc.put("lastName", RandomStringUtils.random(3, "GOOGLE".toCharArray()));
        doc.put("lastUpdate", new Date());
        list.add(doc);
        }
        mongoManager.batchSaveDoc("actor", list);
    }

    @Test
    public void testSaveDocByBean() {
        Actor2 act = new Actor2(new Random().nextInt(10000),
                RandomStringUtils.random(3, "迪丽热巴".toCharArray()),
                RandomStringUtils.random(3, "GOOGLE".toCharArray()),
                new Date());

        mongoManager.saveMongoDoc( act);
        assertNotNull(act.getId());
        System.out.println("id:" + act.getId() + ", doc:" + act);
    }

    @Test
    public void testSaveDocByNormalBean() {
        Actor act = new Actor(new Random().nextInt(10000),
                RandomStringUtils.random(3, "迪丽热巴".toCharArray()),
                RandomStringUtils.random(3, "GOOGLE".toCharArray()),
                new Date());

        mongoManager.saveDoc("actor", act);
        System.out.println(act);
    }

//    @Test
    public void testBatchSaveDocByBean() {
        List<Actor2> list = new ArrayList<>(5);
        for (int i = 0; i < 5; i++) {
            list.add(new Actor2(new Random().nextInt(10000),
                    RandomStringUtils.random(3, "迪丽热巴".toCharArray()),
                    RandomStringUtils.random(3, "GOOGLE".toCharArray()),
                    new Date()));
        }
        mongoManager.batchSaveMongoDoc(list);
    }

    @Test
    public void testQueryById() {
        Actor2 act = new Actor2(new Random().nextInt(10000),
                RandomStringUtils.random(3, "迪丽热巴".toCharArray()),
                RandomStringUtils.random(3, "GOOGLE".toCharArray()),
                new Date());

        mongoManager.saveMongoDoc( act);
        assertNotNull(act.getId());
        Map<String, Object> map = mongoManager.queryDocById("actor", act.getId());
        System.out.println(map);
        assertTrue(map != null && !map.isEmpty());
    }

    @Test
    public void testQueryAll() {
        List<Map> list = mongoManager.queryAllDoc("actor");
        System.out.println(list);
        assertTrue(list != null && !list.isEmpty());
        System.out.println(list.size());
        assertEquals(Date.class.getName(), list.get(0).get("lastUpdate").getClass().getName());
    }

    @Test
    public void testQueryOneByMap() {
        Map<String, Object> param = new HashMap<>();
        param.put("lastName", "LOL");
        param.put("firstName", "迪迪热");
        Map doc = mongoManager.queryOneDoc("actor", param);
        System.out.println(doc);
        assertTrue(doc != null && !doc.isEmpty());
    }

    @Test
    public void testQueryOneByMapAndSort() {
        Map<String, Object> param = new HashMap<>();
        param.put("lastName", "LOL");
        Map doc = mongoManager.queryOneDoc("actor", param, null);
        System.out.println(doc);
        assertTrue(doc != null && !doc.isEmpty());
        ObjectId id1 = (ObjectId) doc.get("_id");
        System.out.println(id1.toString());
        assertEquals("5b57188fb081620af4429e90", id1.toString());

        Map doc2 = mongoManager.queryOneDoc("actor", param,
                new Sort(Arrays.asList(new Sort.Order(Sort.Direction.ASC, "actorId"), new Sort.Order(Sort.Direction.DESC, "_id"))));
        System.out.println(doc2);
        assertTrue(doc2 != null && !doc2.isEmpty());
        ObjectId id2 = (ObjectId) doc2.get("_id");
        System.out.println(id2.toString());
        assertEquals("5b57188fb081620af4429e8a", id2.toString());
    }

    @Test
    public void testQueryListByMapNoSort() {
        Map<String, Object> param = new HashMap<>();
        param.put("lastName", "LOL");
        List<Map> docs = mongoManager.queryDocList("actor", param);
        System.out.println(docs);
        assertTrue(docs != null && !docs.isEmpty());
    }

    @Test
    public void testQueryListByMapSort() {
        Map<String, Object> param = new HashMap<>();
        param.put("lastName", "LOL");
        List<Map> docs = mongoManager.queryDocList("actor", param, null);
        System.out.println(docs);
        assertTrue(docs != null && !docs.isEmpty());
    }

    @Test
    public void testQueryMongoDocList() {
        Query q = new Query();
        q.addCriteria(Criteria.where("actorId").gte(9000));
        List<Actor> docs = mongoManager.queryMongoDocList("actor", q, Actor.class, null);
        System.out.println(docs.size());
        System.out.println(docs);
        assertTrue(docs != null && !docs.isEmpty());
    }
}
