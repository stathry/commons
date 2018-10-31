package org.stathry.commons.dao;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.Assert;
import org.stathry.commons.utils.ApplicationContextUtils;

import java.util.List;
import java.util.Map;

/**
 * mongo管理服务
 *
 * @see com.mongodb.MongoClientOptions.Builder
 * @see <p><a href="https://docs.mongodb.com/manual/tutorial/query-documents/">MongoDB doc</a></p>
 * Created by dongdaiming on 2018-07-24 17:32
 */
public class EasyMongoTemplate {

    private static final MongoTemplate mongoTemplate = ApplicationContextUtils.getBean("mongoTemplate", MongoTemplate.class);

    private EasyMongoTemplate() {
    }

    public static MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }

    public static void save(Object doc) {
        mongoTemplate.save(doc);
    }

    public static void save(String collectionName, Object doc) {
        mongoTemplate.save(doc, collectionName);
    }

    public static void save(String collectionName, Map<String, Object> doc) {
        mongoTemplate.save(doc, collectionName);
    }

    public static void insertAll(List<?> docs) {
        mongoTemplate.insertAll(docs);
    }

    public static void insertAll(String collectionName, List<Map<String, Object>> docs) {
        mongoTemplate.insert(docs, collectionName);
    }

    public static JSONObject findById(String collectionName, String docId) {
        return mongoTemplate.findById(docId, JSONObject.class, collectionName);
    }

    public static <T> T findById(String collectionName, String docId, Class<T> resultType) {
        return mongoTemplate.findById(docId, resultType, collectionName);
    }

    // 如果查到多条记录时默认选择id较小的
    public static JSONObject findOne(String collectionName, Map<String, Object> params) {
        return findOne(collectionName, JSONObject.class, params, null);
    }

    public static <T> T findOne(String collectionName, Class<T> resultType, Map<String, Object> params) {
        return findOne(collectionName, resultType, params, null);
    }

    public static <T> T findOne(String collectionName, Class<T> resultType, Map<String, Object> params,
                                Sort.Direction sortDirection, List<String> sortProperties) {
        Query q = new Query(mapToCriteria(params));
        if (sortProperties != null && !sortProperties.isEmpty()) {
            q.with(new Sort(sortDirection, sortProperties));
        }

        return mongoTemplate.findOne(q, resultType, collectionName);
    }

    public static <T> T findOne(String collectionName, Class<T> resultType, Map<String, Object> params, List<Sort.Order> sortOrders) {
        Query q = new Query(mapToCriteria(params));
        if (sortOrders != null && !sortOrders.isEmpty()) {
            q.with(new Sort(sortOrders));
        }

        return mongoTemplate.findOne(q, resultType, collectionName);
    }

    public static <T> T findOne(String collectionName, Class<T> resultType, Query query, Sort sort) {
        if (sort != null) {
            query.with(sort);
        }

        return mongoTemplate.findOne(query, resultType, collectionName);
    }

    public static List<JSONObject> find(String collectionName, Map<String, Object> params) {
        Query q = new Query(mapToCriteria(params));
        return mongoTemplate.find(q, JSONObject.class, collectionName);
    }

    public static <T> List<T> find(String collectionName, Class<T> resultType, Map<String, Object> params) {
        return find(collectionName, resultType, params, null, null);
    }

    public static <T> List<T> find(String collectionName, Class<T> resultType, Map<String, Object> params,
                                   List<String> selectFields) {
        return findWithSort(collectionName, resultType, params, selectFields, null, null, null);
    }

    public static <T> List<T> find(String collectionName, Class<T> resultType, Map<String, Object> params,
                                   List<String> selectFields, Sort.Direction sortDirection, List<String> sortProperties) {
        return findWithSort(collectionName, resultType, params, selectFields, null, sortDirection, sortProperties);
    }

    public static <T> List<T> find(String collectionName, Class<T> resultType, Map<String, Object> params,
                                   List<String> selectFields, List<Sort.Order> orders) {
        return findWithSort(collectionName, resultType, params, selectFields, orders, null, null);
    }

    private static <T> List<T> findWithSort(String collectionName, Class<T> resultType, Map<String, Object> params,
                                    List<String> selectFields, List<Sort.Order> orders,
                                    Sort.Direction sortDirection, List<String> sortProperties) {
        DBObject fdb = (selectFields == null || selectFields.isEmpty()) ? null : toDBObject(selectFields);
        Assert.notEmpty(params, "required params.");
        DBObject qdb = new BasicDBObject(params);

        Query q = fdb == null ? new BasicQuery(qdb) : new BasicQuery(qdb, fdb);

        Sort sort = (orders != null && !orders.isEmpty()) ? new Sort(orders) :
                (sortProperties != null && !sortProperties.isEmpty() ? new Sort(sortDirection, sortProperties) : null);

        if (sort != null) {
            q.with(sort);
        }
        return mongoTemplate.find(q, resultType, collectionName);
    }

    public static <T> List<T> find(String collectionName, Criteria criteria, Class<T> resultType, Sort sort) {
        Query q = new Query(criteria);
        if (sort != null) {
            q.with(sort);
        }
        return mongoTemplate.find(q, resultType, collectionName);
    }

    @Deprecated
    public static List<JSONObject> findAll(String collectionName) {
        return mongoTemplate.findAll(JSONObject.class, collectionName);
    }

    @Deprecated
    public static <T> List<T> findAll(String collectionName, Class<T> resultType) {
        return mongoTemplate.findAll(resultType, collectionName);
    }

    public static List<JSONObject> group(String collectionName, Map<String, Object> params, List<String> groupFields) {
        return group(collectionName, params, groupFields, JSONObject.class, null, null);
    }

    public static <T> List<T> group(String collectionName, Map<String, Object> params, List<String> groupFields, Class<T> resultType) {
        return group(collectionName, params, groupFields, resultType, null, null);
    }

    public static <T> List<T> group(String collectionName, Map<String, Object> params, List<String> groupFields, Class<T> resultType,
                                    Sort.Direction sortDirection, List<String> sortProperties) {
        Criteria c = mapToCriteria(params);
        String[] gf = new String[groupFields.size()];
        groupFields.toArray(gf);
        Sort sort = (sortProperties == null || sortProperties.isEmpty()) ? null : new Sort(sortDirection, sortProperties);
        Aggregation agg = (sort == null) ? Aggregation.newAggregation(Aggregation.match(c), Aggregation.group(gf))
                : Aggregation.newAggregation(Aggregation.match(c), Aggregation.group(gf), Aggregation.sort(sort));

        return mongoTemplate.aggregate(agg, collectionName, resultType).getMappedResults();
    }

    public static Integer countGroup(String collectionName, Map<String, Object> params, List<String> groupFields) {
        return group(collectionName, params, groupFields, JSONObject.class).size();
    }

    private static DBObject toDBObject(List<String> fieldList) {
        BasicDBObject fields = new BasicDBObject();
        fields.put("_id", false);
        for (int i = 0, size = fieldList.size(); i < size; i++) {
            fields.put(fieldList.get(i), true);
        }
        return fields;
    }

    private static Criteria mapToCriteria(Map<String, Object> params) {
        Assert.notEmpty(params, "required params.");
        Criteria c = new Criteria();
        for (Map.Entry<String, Object> p : params.entrySet()) {
            c = c.where(p.getKey()).is(p.getValue());
        }
        return c;
    }

}
