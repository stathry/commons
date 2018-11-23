package org.stathry.commons.dao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;
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
 * MongoTest
 * Created by dongdaiming on 2018-07-24 17:45
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-context.xml")
public class MongoTest {

    private static final String COLL_NAME = "player";
    private static final String FIRST_NAMES = "TYRION";
    private static final String LAST_NAMES = "LANNISTER";

    private Map<String, Object> initRandomPlayerMap() {
        Map<String, Object> doc = new HashMap<>(8);
        doc.put("playId", new Random().nextInt(10000));
        doc.put("firstName", RandomStringUtils.random(3, FIRST_NAMES.toCharArray()));
        doc.put("lastName", RandomStringUtils.random(3, LAST_NAMES.toCharArray()));
        doc.put("lastUpdate", new Date());
        return doc;
    }

    private Player initRandomPlayer() {
        return new Player(new Random().nextInt(10000),
                RandomStringUtils.random(3, FIRST_NAMES.toCharArray()),
                RandomStringUtils.random(3, LAST_NAMES.toCharArray()),
                new Date());
    }

    private void insertAllWithSize(int size, List<Map<String, Object>> list, Integer hid, String lastName) throws InterruptedException {
        Map<String, Object> doc;
        for (int i = 0; i < size; i++) {
            doc = new HashMap<>(8);
            doc.put("playId", hid);
            doc.put("firstName", RandomStringUtils.random(3, FIRST_NAMES.toCharArray()));
            doc.put("lastName", lastName);
            doc.put("lastUpdate", new Date());
            list.add(doc);
            Thread.sleep(10);
        }
        list.add(initRandomPlayerMap());
        System.out.println(JSON.toJSONString(list));
        EasyMongoTemplate.insertAll(COLL_NAME, list);
    }


    @Test
    public void testSave() {
        Map<String, Object> doc = initRandomPlayerMap();
        System.out.println(doc);
        EasyMongoTemplate.save(COLL_NAME, doc);

        Player player = initRandomPlayer();
        System.out.println(player);
        EasyMongoTemplate.save(player);

        JSONObject player2 = new JSONObject();
        player2.put("playId", 666);
        System.out.println(player2);
        EasyMongoTemplate.save(COLL_NAME, player2);
    }

    @Test
    public void testInsertAll() {
        List<Player> list = new ArrayList<>(5);
        for (int i = 0; i < 5; i++) {
            list.add(new Player(new Random().nextInt(10000),
                    RandomStringUtils.random(3, FIRST_NAMES.toCharArray()),
                    RandomStringUtils.random(3, LAST_NAMES.toCharArray()),
                    new Date()));
        }
        System.out.println(JSON.toJSONString(list));
        EasyMongoTemplate.insertAll(list);
    }

    @Test
    public void testFindById() {
        Player act = initRandomPlayer();
        System.out.println(act);
        EasyMongoTemplate.save(act);
        assertNotNull(act.getId());

        JSONObject map = EasyMongoTemplate.findById(COLL_NAME, act.getId());
        System.out.println(map);
        assertTrue(map != null && !map.isEmpty());

        Player p1 = EasyMongoTemplate.findById(COLL_NAME, act.getId(), Player.class);
        System.out.println(p1);
        assertNotNull(p1);
        System.out.println(p1.getId());
    }

    @Test
    public void testFindOne() {
        Player act = initRandomPlayer();
        System.out.println(act);
        EasyMongoTemplate.save(act);
        assertNotNull(act.getId());

        Map<String, Object> params = new HashMap<>();
        params.put("playId", act.getId());

        JSONObject map = EasyMongoTemplate.findOne(COLL_NAME, params);
        System.out.println(map);
        assertTrue(map != null && !map.isEmpty());

        Player p1 = EasyMongoTemplate.findOne(COLL_NAME, Player.class, params);
        System.out.println(p1);
        assertNotNull(p1);
        System.out.println(p1.getId());
    }

    @Test
    public void testFindAll() {
        List<JSONObject> list = EasyMongoTemplate.findAll(COLL_NAME);
        System.out.println(JSON.toJSONString(list));
        assertTrue(list != null && !list.isEmpty());
        System.out.println(list.size());
        assertEquals(Date.class.getName(), list.get(0).get("lastUpdate").getClass().getName());

        List<Player> list2 = EasyMongoTemplate.findAll(COLL_NAME, Player.class);
        System.out.println(JSON.toJSONString(list2));
        assertTrue(list2 != null && !list2.isEmpty());
        System.out.println(list2.size());
    }

    @Test
    public void testFind() throws InterruptedException {
        int size = 6;
        List<Map<String, Object>> list = new ArrayList<>(size);
        Integer hid = new Random().nextInt(10000);
        String lastName = RandomStringUtils.random(3, LAST_NAMES.toCharArray());
        insertAllWithSize(size, list, hid, lastName);

        Map<String, Object> param = new HashMap<>();
        param.put("playId", hid);
        param.put("lastName", lastName);
        List<JSONObject> list2 = EasyMongoTemplate.find(COLL_NAME, param);
        System.out.println(list2);
        assertTrue(list2 != null && !list2.isEmpty());
        assertEquals(size, list2.size());
    }

    @Test
    public void testFindSelectFields() {
        Map<String, Object> params = new HashMap<>();
        params.put("playId", 609);
        List<String> selectFields = Arrays.asList("playId", "firstName", "lastUpdate");

        List<JSONObject> docs = EasyMongoTemplate.find(COLL_NAME, JSONObject.class, params, selectFields, null);
        System.out.println(JSON.toJSONString(docs));
        assertTrue(docs != null && !docs.isEmpty());
        assertTrue(!docs.get(0).isEmpty());
        assertEquals(3, docs.get(0).size());

        List<Player> list = EasyMongoTemplate.find(null, Player.class, params, selectFields, null);
        System.out.println(JSON.toJSONString(list));
        assertTrue(list != null && !list.isEmpty());
        Player p = list.get(0);
        assertNotNull(p);
        assertTrue(StringUtils.isNotBlank(p.getFirstName()));
        assertNotNull(p.getPlayId());
        assertTrue(StringUtils.isBlank(p.getLastName()));
    }

    @Test
    public void testFindSort() {
        Map<String, Object> params = new HashMap<>();
        params.put("playId", 609);
        List<String> selectFields = Arrays.asList("playId", "firstName", "lastUpdate");

        List<JSONObject> docs = EasyMongoTemplate.find(COLL_NAME, JSONObject.class, params, selectFields, Sort.Direction.DESC, Arrays.asList("lastUpdate"));
        System.out.println(JSON.toJSONString(docs));
        assertTrue(docs != null && !docs.isEmpty());
        assertTrue(!docs.get(0).isEmpty());
        assertEquals(3, docs.get(0).size());

        List<Player> list = EasyMongoTemplate.find(null, Player.class, params, selectFields, Sort.Direction.ASC, Arrays.asList("lastUpdate"));
        System.out.println(JSON.toJSONString(list));
        assertTrue(list != null && !list.isEmpty());
        Player p = list.get(0);
        assertNotNull(p);
        assertTrue(StringUtils.isNotBlank(p.getFirstName()));
        assertNotNull(p.getPlayId());
        assertTrue(StringUtils.isBlank(p.getLastName()));
    }

    @Test
    public void testGroup() {
        Map<String, Object> params = new HashMap<>();
        params.put("lastName", "ELA");

        List<JSONObject> docs = EasyMongoTemplate.group(COLL_NAME, params, Arrays.asList("playId", "firstName"));
        assertTrue(docs != null && !docs.isEmpty());
        assertTrue(!docs.get(0).isEmpty());
        System.out.println(JSON.toJSONString(docs));
    }

    @Test
    public void testCountGroup() {
        Map<String, Object> params = new HashMap<>();
        params.put("lastName", "ELA");

        Integer n = EasyMongoTemplate.countGroup(COLL_NAME, params, Arrays.asList("playId", "firstName"));
        assertNotNull(n);
        System.out.println(n);
    }

    @Document(collection = "player")
    private static class Player {

        @Id
        private String id;
        /**  */
        private Integer playId;

        /**  */
        private String firstName;

        /**  */
        private String lastName;

        /**  */
        private Date lastUpdate;

        public Player(Integer actorId, String firstName, String lastName, Date lastUpdate) {
            this.playId = actorId;
            this.firstName = firstName;
            this.lastName = lastName;
            this.lastUpdate = lastUpdate;
        }

        public Player() {
        }

        public Integer getPlayId() {
            return playId;
        }

        public void setPlayId(Integer playId) {
            this.playId = playId;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public Date getLastUpdate() {
            return lastUpdate;
        }

        public void setLastUpdate(Date lastUpdate) {
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
            Player actor = (Player) o;
            return Objects.equals(playId, actor.playId) &&
                    Objects.equals(firstName, actor.firstName) &&
                    Objects.equals(lastName, actor.lastName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(playId, firstName, lastName);
        }

        @Override
        public String toString() {
            return "Player{" +
                    "id='" + id + '\'' +
                    ", playId=" + playId +
                    ", firstName='" + firstName + '\'' +
                    ", lastName='" + lastName + '\'' +
                    ", lastUpdate=" + lastUpdate +
                    '}';
        }
    }

}
