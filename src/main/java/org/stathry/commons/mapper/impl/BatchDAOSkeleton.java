package org.stathry.commons.mapper.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Assert;
import org.stathry.commons.mapper.GenericMapper;
import org.stathry.commons.utils.ApplicationContextUtils;

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
        Assert.notEmpty(list, "required list");
        Assert.isTrue(batchSize > 0, "batchSize > 0");

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

    public static <T, DAO extends GenericMapper> Integer mybatisBatchInsert(List<T> list, int batchSize, DAO dao) {
        Assert.notEmpty(list, "required list");
        Assert.isTrue(batchSize > 0, "batchSize > 0");

        int batch = batchSize, size = list.size(), c = 0;
        int to = size < batch ? size : batch;
        String name = list.get(0).getClass().getSimpleName();
        for (int from = 0; from < to && to <= size;) {
            c += dao.insertAll(list.subList(from, to));
            LOGGER.info("insert {}List, fromIndex {}, toIndex {}.", name, from, to);
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
