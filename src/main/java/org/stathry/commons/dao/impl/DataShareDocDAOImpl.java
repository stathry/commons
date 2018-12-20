package org.stathry.commons.dao.impl;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import org.stathry.commons.mapper.impl.GenericMapperImpl;
import org.stathry.commons.model.DataShareDoc;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 统一第三方文件信息访问
 *
 * @author dongdaiming
 * @date 2018/3/20
 */
//@Repository
public class DataShareDocDAOImpl extends GenericMapperImpl<DataShareDoc, Integer> implements DataShareDocDAO {

    private static final String COLUMNS = "SELECT id, doc_name, status, fail_count, doc_size, records, doc_path, add_time, update_time ";

    @Override
    public List<DataShareDoc> listDoc(String dataGroup, Integer status, Integer pageSize, Integer maxFailCount) {

        StringBuilder sql = new StringBuilder();
        sql.append(COLUMNS).append(" FROM data_share_doc ")
                .append(" where data_group =:dataGroup and status=:status and fail_count <= :maxFailCount order by id asc limit :pageSize");

        Map<String, Object> paramMap = new HashMap<>(4);
        paramMap.put("dataGroup", dataGroup);
        paramMap.put("status", status);
        paramMap.put("pageSize", pageSize);
        paramMap.put("maxFailCount", maxFailCount);
        return namedParameterJdbcTemplate.query(sql.toString(), paramMap,
                new BeanPropertyRowMapper<>(DataShareDoc.class));
    }

    @Override
    public List<DataShareDoc> listDoc(String dataGroup, Integer status, Date bizDate, Integer pageSize, Integer maxFailCount) {
        StringBuilder sql = new StringBuilder();
        sql.append(COLUMNS).append(" FROM data_share_doc ")
                .append(" where data_group =:dataGroup and biz_date=:bizDate and status=:status and fail_count <= :maxFailCount order by id asc limit :pageSize");

        Map<String, Object> paramMap = new HashMap<>(8);
        paramMap.put("data_group", dataGroup);
        paramMap.put("bizDate", bizDate);
        paramMap.put("status", status);
        paramMap.put("pageSize", pageSize);
        paramMap.put("maxFailCount", maxFailCount);
        return namedParameterJdbcTemplate.query(sql.toString(), paramMap,
                new BeanPropertyRowMapper<>(DataShareDoc.class));
    }

    @Override
    public Integer updateStatusById(Long id, Integer sourceStatus, Integer targetStatus, Integer failCount) {
        String sql = "update data_share_doc set status =:targetStatus, fail_count=:failCount where id=:id and status=:sourceStatus";

        Map<String, Object> paramMap = new HashMap<>(4);
        paramMap.put("id", id);
        paramMap.put("targetStatus", targetStatus);
        paramMap.put("sourceStatus", sourceStatus);
        paramMap.put("failCount", failCount);
        return namedParameterJdbcTemplate.update(sql, paramMap);
    }
}
