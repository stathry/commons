package org.stathry.commons.mapper.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * 批量数据操作骨架
 * Created by dongdaiming on 2018-08-16 11:08
 */
public class BatchDAOSkeleton {

    private static final Logger LOGGER = LoggerFactory.getLogger(BatchDAOSkeleton.class);

    public static <T extends BatchInsertion> Integer jdbcBatchInsert(JdbcTemplate jdbcTemplate, List<T> list, String sql, int batchSize) {
        if(list == null || list.isEmpty()) {
            return 0;
        }

        int size = list.size();
        List<Object[]> args = new ArrayList<>(batchSize);
        T c;
        for (int i = 0, last = size - 1; i < size; i++) {
            c = list.get(i);
            args.add(c.toArgArray());

            if ((i != 0 && i % batchSize == 0) || i == last) {
                jdbcTemplate.batchUpdate(sql, args);
                LOGGER.info("insertList, index {}", i);

                args = i == last ? args : new ArrayList<Object[]>(batchSize);
            }

        }
        return size;
    }

    /**
     * 可批量插入接口
     */
    public static interface BatchInsertion {

        /**
         * javaBean转换为insert value对应的参数数组
         */
        Object[] toArgArray();
    }
}
