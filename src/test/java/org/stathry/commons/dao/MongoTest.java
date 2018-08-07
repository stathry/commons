package org.stathry.commons.dao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.stathry.commons.pojo.Hero;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * TODO
 * Created by dongdaiming on 2018-07-24 17:45
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-context.xml")
public class MongoTest {

    @Autowired
    private MongoManager mongoManager;

    private static final String COLL_NAME = "hero";
    private static final String FIRST_NAMES = "TYRION";
    private static final String LAST_NAMES = "LANNISTER";

    private Map<String, Object> initDocWithMap() {
        Map<String, Object> doc = new HashMap<>(8);
        doc.put("heroId", new Random().nextInt(10000));
        doc.put("firstName", RandomStringUtils.random(3, FIRST_NAMES.toCharArray()));
        doc.put("lastName", RandomStringUtils.random(3, LAST_NAMES.toCharArray()));
        doc.put("lastUpdate", new Date());
        return doc;
    }

    private void batchSaveWithIdAndName(int size, List<Map<String, Object>> list, Integer hid, String lastName) throws InterruptedException {
        Map<String, Object> doc;
        for (int i = 0; i < size; i++) {
            doc = new HashMap<>(8);
            doc.put("heroId", hid);
            doc.put("firstName", RandomStringUtils.random(3, FIRST_NAMES.toCharArray()));
            doc.put("lastName", lastName);
            doc.put("lastUpdate", new Date());
            list.add(doc);
            Thread.sleep(10);
        }
        list.add(initDocWithMap());
        mongoManager.batchSaveDoc(COLL_NAME, list);
    }

    @Test
    public void testSaveDocByMap() {
        Map<String, Object> doc = initDocWithMap();
        mongoManager.saveDoc(COLL_NAME, doc);
        System.out.println(doc);
    }

    //    @Test
    public void testBatchSaveDocByMap() {
        List<Map<String, Object>> list = new ArrayList<>(5);
        for (int i = 0; i < 5; i++) {
            list.add(initDocWithMap());
        }
        mongoManager.batchSaveDoc(COLL_NAME, list);
    }

    @Test
    public void testSaveDocByBean() {
        Hero act = initDocWithBean();
        mongoManager.saveMongoDoc(act);
        assertNotNull(act.getId());
        System.out.println("id:" + act.getId() + ", doc:" + act);
    }

    private Hero initDocWithBean() {
        return new Hero(new Random().nextInt(10000),
                RandomStringUtils.random(3, FIRST_NAMES.toCharArray()),
                RandomStringUtils.random(3, LAST_NAMES.toCharArray()),
                new Date());
    }

    @Test
    public void testSaveDocByNormalBean() {
        Hero act = new Hero(new Random().nextInt(10000),
                RandomStringUtils.random(3, FIRST_NAMES.toCharArray()),
                RandomStringUtils.random(3, LAST_NAMES.toCharArray()),
                new Date());

        mongoManager.saveDoc(COLL_NAME, act);
        System.out.println(act);
    }

    //    @Test
    public void testBatchSaveDocByBean() {
        List<Hero> list = new ArrayList<>(5);
        for (int i = 0; i < 5; i++) {
            list.add(new Hero(new Random().nextInt(10000),
                    RandomStringUtils.random(3, FIRST_NAMES.toCharArray()),
                    RandomStringUtils.random(3, LAST_NAMES.toCharArray()),
                    new Date()));
        }
        mongoManager.batchSaveMongoDoc(list);
    }

    @Test
    public void testQueryById() {
        Hero act = initDocWithBean();
        mongoManager.saveMongoDoc(act);
        assertNotNull(act.getId());

        JSONObject map = mongoManager.queryDocById(COLL_NAME, act.getId());
        System.out.println(map);
        assertTrue(map != null && !map.isEmpty());
    }

    @Test
    public void testQueryAll() {
        List<JSONObject> list = mongoManager.queryAllDoc(COLL_NAME);
        System.out.println(list);
        assertTrue(list != null && !list.isEmpty());
        System.out.println(list.size());
        assertEquals(Date.class.getName(), list.get(0).get("lastUpdate").getClass().getName());
    }

    @Test
    public void testQueryOneByMap() throws InterruptedException {
        int size = 3;
        List<Map<String, Object>> list = new ArrayList<>(size);
        Integer hid = new Random().nextInt(10000);
        String lastName = RandomStringUtils.random(3, LAST_NAMES.toCharArray());
        batchSaveWithIdAndName(size, list, hid, lastName);
        
        Map<String, Object> param = new HashMap<>();
        param.put("heroId", hid);
        param.put("lastName", lastName);
        Map doc2 = mongoManager.queryOneDoc(COLL_NAME, param);
        System.out.println(doc2);
        assertTrue(doc2 != null && !doc2.isEmpty());
    }

    @Test
    public void testQueryOneByMapAndSort() throws InterruptedException {
        int size = 3;
        List<Map<String, Object>> list = new ArrayList<>(size);
        Integer hid = new Random().nextInt(10000);
        String lastName = RandomStringUtils.random(3, LAST_NAMES.toCharArray());
        batchSaveWithIdAndName(size, list, hid, lastName);

        Map<String, Object> param = new HashMap<>();
        param.put("heroId", hid);
        param.put("lastName", lastName);
        Map doc2 = mongoManager.queryOneDoc(COLL_NAME, param,
                new Sort(Arrays.asList(new Sort.Order(Sort.Direction.ASC, "lastUpdate"), new Sort.Order(Sort.Direction.DESC, "firstName"))));
        System.out.println(doc2);
        assertNotNull(doc2);
        assertEquals(doc2.get("firstName"), list.get(0).get("firstName"));
        assertEquals(doc2.get("lastUpdate"), list.get(0).get("lastUpdate"));
    }

    @Test
    public void testQueryListByMapNoSort() throws InterruptedException {
        int size = 3;
        List<Map<String, Object>> list = new ArrayList<>(size);
        Integer hid = new Random().nextInt(10000);
        String lastName = RandomStringUtils.random(3, LAST_NAMES.toCharArray());
        batchSaveWithIdAndName(size, list, hid, lastName);

        Map<String, Object> param = new HashMap<>();
        param.put("heroId", hid);
        param.put("lastName", lastName);
        List<JSONObject> docs = mongoManager.queryDocList(COLL_NAME, param);
        System.out.println(docs);
        assertTrue(docs != null && !docs.isEmpty());
        System.out.println(docs.size());
        assertTrue(docs.size() >= size);
    }

    @Test
    public void testQueryListByMapSort() throws InterruptedException {
        int size = 3;
        List<Map<String, Object>> list = new ArrayList<>(size);
        Integer hid = new Random().nextInt(10000);
        String lastName = RandomStringUtils.random(3, LAST_NAMES.toCharArray());
        batchSaveWithIdAndName(size, list, hid, lastName);

        Map<String, Object> param = new HashMap<>();
        param.put("heroId", hid);
        param.put("lastName", lastName);

        Collections.sort(list, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                int c1 = o1.get("firstName").toString().compareTo(o2.get("firstName").toString());
                if(c1 != 0) {
                    return c1;
                }
                Date d1 = (Date)o1.get("lastUpdate");
                Date d2 = (Date)o2.get("lastUpdate");
                return d1.compareTo(d2);
            }
        });
        list.remove(list.size() - 1);
        List<JSONObject> docs = mongoManager.queryDocList(COLL_NAME, param,
                new Sort(Arrays.asList(new Sort.Order(Sort.Direction.ASC, "firstName"), new Sort.Order(Sort.Direction.DESC, "lastUpdate"))));
        System.out.println(docs);
        assertTrue(docs != null && !docs.isEmpty());
        System.out.println(docs.size());
        assertTrue(docs.size() >= size);
        for (int i = 0; i < list.size(); i++) {
        assertEquals(list.get(i).get("heroId"), docs.get(i).get("heroId"));
        assertEquals(list.get(i).get("firstName"), docs.get(i).get("firstName"));
        assertEquals(list.get(i).get("lastName"), docs.get(i).get("lastName"));
        assertEquals(list.get(i).get("lastUpdate"), docs.get(i).get("lastUpdate"));
        }
    }

    @Test
    public void testQueryMongoDocListByRange() throws InterruptedException {
        int size = 3;
        List<Map<String, Object>> list = new ArrayList<>(size);
        Integer hid = new Random().nextInt(10000);
        String lastName = RandomStringUtils.random(3, LAST_NAMES.toCharArray());
        batchSaveWithIdAndName(size, list, hid, lastName);

        Query q = new Query();
        q.addCriteria(Criteria.where("heroId").gte(hid));
//        q.addCriteria(Criteria.where("heroId").gte(3021));
        List<Hero> docs = mongoManager.queryMongoDocList(COLL_NAME, q, Hero.class, null);
        System.out.println(docs.size());
        System.out.println(docs);
        assertTrue(docs != null && !docs.isEmpty());
        assertTrue(docs.size() >= size);
    }

    @Test
    public void testQueryMongoDocListByCollection() throws InterruptedException {
        int size = 3;
        List<Map<String, Object>> list = new ArrayList<>(size);
        Integer hid = new Random().nextInt(10000);
        String lastName = RandomStringUtils.random(3, LAST_NAMES.toCharArray());
        batchSaveWithIdAndName(size, list, hid, lastName);
        System.out.println(list);

        Query q = new Query();
        q.addCriteria(Criteria.where("heroId").in(hid, list.get(list.size() - 1).get("heroId")));
//        q.addCriteria(Criteria.where("firstName").in("OYN", "ONI"));
        List<Hero> docs = mongoManager.queryMongoDocList(COLL_NAME, q, Hero.class, null);
        System.out.println(docs.size());
        System.out.println(docs);
        assertTrue(docs != null && !docs.isEmpty());
        assertTrue(docs.size() >= size);
    }

    @Test
    public void testQueryMongoDocListByMultiCondition() throws InterruptedException {
        int size = 3;
        List<Map<String, Object>> list = new ArrayList<>(size);
        Integer hid = new Random().nextInt(10000);
        String lastName = RandomStringUtils.random(3, LAST_NAMES.toCharArray());
        batchSaveWithIdAndName(size, list, hid, lastName);
        System.out.println("hid:" + hid + ", lastName:" + lastName);
        System.out.println(list);

        Query q = new Query();
        q.addCriteria(Criteria.where("heroId").is(hid).and("lastName").is(lastName));
//        q.addCriteria(Criteria.where("heroId").is(hid).and("lastName").is(lastName).and("firstName").is(list.get(0).get("firstName")));
        List<Hero> docs = mongoManager.queryMongoDocList(COLL_NAME, q, Hero.class, null);
        System.out.println(docs.size());
        System.out.println(docs);
        assertTrue(docs != null && !docs.isEmpty());
        assertTrue(docs.size() >= size);
    }

    @Test
    public void testQueryDocListByFields() {
        DBObject params = new BasicDBObject();
        params.put("lastName", "NAS");  //查询条件

        BasicDBObject fields = new BasicDBObject();
        fields.put("heroId", true);
        fields.put("_id", false);
        List<JSONObject> docs = mongoManager.queryDocListByFields(COLL_NAME, params, fields, null);
        assertTrue(docs != null && !docs.isEmpty());
        System.out.println(JSON.toJSONString(docs));
    }

    @Document(collection = "hero")
    private static class Hero {

        @Id
        private String id;
        /**  */
        private Integer heroId;

        /**  */
        private String firstName;

        /**  */
        private String lastName;

        /**  */
        private Date lastUpdate;

        public Hero(Integer actorId, String firstName, String lastName, Date lastUpdate) {
            this.heroId = actorId;
            this.firstName = firstName;
            this.lastName = lastName;
            this.lastUpdate = lastUpdate;
        }

        public Hero() {
        }

        public Integer getHeroId(){
            return heroId;
        }

        public void setHeroId(Integer heroId){
            this.heroId = heroId;
        }

        public String getFirstName(){
            return firstName;
        }

        public void setFirstName(String firstName){
            this.firstName = firstName;
        }

        public String getLastName(){
            return lastName;
        }

        public void setLastName(String lastName){
            this.lastName = lastName;
        }

        public Date getLastUpdate(){
            return lastUpdate;
        }

        public void setLastUpdate(Date lastUpdate){
            this.lastUpdate = lastUpdate;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Hero actor = (Hero) o;
            return Objects.equals(heroId, actor.heroId) &&
                    Objects.equals(firstName, actor.firstName) &&
                    Objects.equals(lastName, actor.lastName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(heroId, firstName, lastName);
        }

        @Override
        public String toString() {
            return "Hero{" +
                    "id='" + id + '\'' +
                    ", heroId=" + heroId +
                    ", firstName='" + firstName + '\'' +
                    ", lastName='" + lastName + '\'' +
                    ", lastUpdate=" + lastUpdate +
                    '}';
        }
    }

}
