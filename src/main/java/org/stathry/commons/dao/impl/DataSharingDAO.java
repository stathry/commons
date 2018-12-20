package org.stathry.commons.dao.impl;

import org.stathry.commons.mapper.impl.BatchDAOSkeleton;
import org.stathry.commons.model.dto.DataRange;

import java.util.List;
import java.util.Map;

/**
 * 资信共享数据访问
 * @author dongdaiming
 * @date 2018年1月2日
 */
public interface DataSharingDAO {

	List<Map<String, Object>> dynamicQueryDataList(String columns, String tableName, String keyColumn, Long id1, Long id2);
	
	DataRange<Long> queryKeyRange(String tableName, String keyColumn);

    DataRange<Long> queryKeyRange(String tableName, String keyColumn, String dateColumn, String beginDate, String endDate);

    <T extends BatchDAOSkeleton.BatchInsertion> Integer jdbcBatchInsert(List<T> list, String sql, int batchSize);

}
