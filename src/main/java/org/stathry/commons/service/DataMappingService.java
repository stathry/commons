package org.stathry.commons.service;

import org.stathry.commons.model.dto.DataMap;
import org.stathry.commons.model.dto.DataRange;

/**
 * 数据映射接口
 * Created by dongdaiming on 2018-12-04 11:06
 */
public interface DataMappingService {

    DataMap dataMap(String dataGroup);

    DataRange<Long> keyRange(DataMap dataMap);

    DataRange<Long> keyRange(String tableName, String keyColumn);

    DataRange<Long> keyRange(String tableName, String keyColumn, String dateColumn, String beginDate, String endDate);

}
