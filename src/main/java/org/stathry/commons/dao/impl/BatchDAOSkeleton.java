package org.stathry.commons.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.stathry.commons.dao.GenericDAO;
import org.stathry.commons.utils.ApplicationContextUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 批量数据操作骨架
 * Created by dongdaiming on 2018-08-16 11:08
 */
public class BatchDAOSkeleton {

    private static final Logger LOGGER = LoggerFactory.getLogger(BatchDAOSkeleton.class);

    private static final JdbcTemplate jdbcTemplate;

    static {
        jdbcTemplate = ApplicationContextUtils.getBean("jdbcTemplate", JdbcTemplate.class);
    }

    public static <T extends BatchInsertion> Integer jdbcBatchInsert(List<T> list, String sql, int batchSize) {
        int size = list.size();
        List<Object[]> args = new ArrayList<>(batchSize);
        T c;
        for (int i = 0, last = size - 1; i < size; i++) {
            c = list.get(i);
            args.add(c.toArgArray());

            if ((i != 0 && i % batchSize == 0) || i == last) {
                jdbcTemplate.batchUpdate(sql, args);
                LOGGER.info("insertList, index {}", i);

                args = i == last ? args : new ArrayList<>(batchSize);
            }

        }
        return size;
    }

    public static <T, DAO extends GenericDAO> Integer mybatisBatchInsert(List<T> list, int batchSize, DAO genericDAO) {
        int batch = batchSize, size = list.size(), c = 0;
        int to = size < batch ? size : batch;
        for (int from = 0; from < to && to <= size;) {
            c += genericDAO.insertAll(list.subList(from, to));
            from = to;
            to += batch;
            to = to > size ? size : to;
        }
        return c;
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
