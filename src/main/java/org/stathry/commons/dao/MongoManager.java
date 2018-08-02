package org.stathry.commons.dao;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.DBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * mongo管理服务
 * @see com.mongodb.MongoClientOptions.Builder
 * @see <p><a href="https://docs.mongodb.com/manual/tutorial/query-documents/">MongoDB doc</a></p>
 * Created by dongdaiming on 2018-07-24 17:32
 */
@Repository
public class MongoManager {

    @Autowired
    MongoTemplate mongoTemplate;

    public void saveMongoDoc(Object doc) {
        mongoTemplate.save(doc);
    }

    public void batchSaveMongoDoc(List<?> docs) {
        mongoTemplate.insertAll(docs);
    }

    public void saveDoc(String collectionName, Map<String, Object> doc) {
        mongoTemplate.save(doc, collectionName);
    }

    public void batchSaveDoc(String collectionName, List<Map<String, Object>> docs) {
        mongoTemplate.insert(docs, collectionName);
    }

    public void saveDoc(String collectionName, Object doc) {
        mongoTemplate.save(doc, collectionName);
    }

    public JSONObject queryDocById(String collectionName, String docId) {
        return mongoTemplate.findById(docId, JSONObject.class, collectionName);
    }

    public JSONObject queryOneDoc(String collectionName, Map<String, Object> param) {
        Query q = new Query();
        for (Map.Entry<String, Object> e : param.entrySet()) {
            q.addCriteria(Criteria.where(e.getKey()).is(e.getValue()));
        }
        return mongoTemplate.findOne(q, JSONObject.class, collectionName);
    }

    public JSONObject queryOneDoc(String collectionName, Map<String, Object> param, Sort sort) {
        Query q = new Query();
        for (Map.Entry<String, Object> e : param.entrySet()) {
            q.addCriteria(Criteria.where(e.getKey()).is(e.getValue()));
        }
        sort = sort == null ? new Sort(Sort.Direction.DESC, "_id") : sort;
        q.with(sort);

        return mongoTemplate.findOne(q, JSONObject.class, collectionName);
    }

    public List<JSONObject> queryDocList(String collectionName, Map<String, Object> param) {
        Query q = new Query();
        for (Map.Entry<String, Object> e : param.entrySet()) {
            q.addCriteria(Criteria.where(e.getKey()).is(e.getValue()));
        }
        return mongoTemplate.find(q, JSONObject.class, collectionName);
    }

    public List<JSONObject> queryDocList(String collectionName, Map<String, Object> param, Sort sort) {
        Query q = new Query();
        for (Map.Entry<String, Object> e : param.entrySet()) {
            q.addCriteria(Criteria.where(e.getKey()).is(e.getValue()));
        }
        sort = sort == null ? new Sort(Sort.Direction.DESC, "_id") : sort;
        q.with(sort);
        return mongoTemplate.find(q, JSONObject.class, collectionName);
    }

    public List<JSONObject> queryDocListByFields(String collectionName, DBObject params, DBObject fields, Sort sort) {
        Query q = new BasicQuery(params, fields);
        sort = sort == null ? new Sort(Sort.Direction.DESC, "_id") : sort;
        q.with(sort);
        return mongoTemplate.find(q, JSONObject.class, collectionName);
    }

    public <T> List<T> queryMongoDocList(String collectionName, Query q, Class<T> mongoDocType, Sort sort) {
        sort = sort == null ? new Sort(Sort.Direction.DESC, "_id") : sort;
        q.with(sort);
        return mongoTemplate.find(q, mongoDocType, collectionName);
    }

    public List<JSONObject> queryAllDoc(String collectionName) {
        return mongoTemplate.findAll(JSONObject.class, collectionName);
    }

}