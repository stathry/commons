package org.stathry.commons.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.stathry.commons.dao.DataSharingDAO;
import org.stathry.commons.mapper.impl.BatchDAOSkeleton;
import org.stathry.commons.model.dto.DataRange;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 共享数据访问
 * @author dongdaiming
 */
@Repository
public class DataSharingDAOImpl implements DataSharingDAO {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DataSharingDAOImpl.class);
	
    @Autowired
    protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Autowired
    protected JdbcTemplate jdbcTemplate;
    
    private static final String RANGE_SQL = " SELECT MIN(%s) minKey, MAX(%s) maxKey FROM %s";
    private static final String RANGE_SQL_DATE = " SELECT MIN(%s) minKey, MAX(%s) maxKey FROM %s WHERE %s >= '%s' and %s < '%s'";

	@Override
	public List<Map<String, Object>> dynamicQueryDataList(String columns, String tableName, String keyColumn, Long id1, Long id2) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ").append(columns).append(" FROM ").append(tableName)
			.append(" WHERE ").append(keyColumn).append(" BETWEEN :id1 AND :id2 ");
		Map<String, Object> paramMap = new HashMap<>(2);
		paramMap.put("id1", id1);
		paramMap.put("id2", id2);
		List<Map<String, Object>> list = null;
		try {
			list = namedParameterJdbcTemplate.queryForList(sql.toString(), paramMap);
		} catch (DataAccessException e) {
			LOGGER.error("dynamicQueryDataList error.", e);
		}	
		list = list == null ? new ArrayList<Map<String, Object>>(0) : list;
		return list;
		}
	
	@Override
	public DataRange<Long> queryKeyRange(String tableName, String keyColumn) {
        return parseKeyRange(namedParameterJdbcTemplate.queryForMap(String.format(RANGE_SQL, keyColumn, keyColumn, tableName), new HashMap<String, Object>(0)));
	}

    @Override
    public DataRange<Long> queryKeyRange(String tableName, String keyColumn, String dateColumn, String beginDate, String endDate) {
        return parseKeyRange(namedParameterJdbcTemplate.queryForMap(String.format(RANGE_SQL_DATE, keyColumn, keyColumn, tableName, dateColumn, beginDate, dateColumn, endDate), new HashMap<String, Object>(0)));
    }

    private DataRange<Long> parseKeyRange(Map<String, Object> rangeMap) {
        Long min = 1L, max = 2000L;
        if (rangeMap != null) {
            Object minKey = rangeMap.get("minKey");
            Object maxKey = rangeMap.get("maxKey");
            min = minKey == null ? min : ((Number) minKey).longValue();
            max = maxKey == null ? max : ((Number) maxKey).longValue();
        }
        DataRange<Long> range = new DataRange<>();
        range.setMin(min);
        range.setMax(max);
        return range;
    }

    @Override
    public <T extends BatchDAOSkeleton.BatchInsertion> Integer jdbcBatchInsert(List<T> list, String sql, int batchSize) {
        return BatchDAOSkeleton.jdbcBatchInsert(list, sql, batchSize);
    }

}
