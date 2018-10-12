package org.stathry.commons.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.stathry.commons.utils.ApplicationContextUtils;
import org.stathry.commons.utils.DBUtils;

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

    public static <T extends BatchInsertion> Integer batchSave(List<T> list, String sql, int batchSize) {
        int size = list.size();
        List<Object[]> args = new ArrayList<>(batchSize);
        T c;
        for(int i = 0, last = size - 1; i < size; i++) {
            c = list.get(i);
            if((i != 0 && i % batchSize == 0)) {
                jdbcTemplate.batchUpdate(sql, args);
                LOGGER.info("batchInsert, index {}", i);
            } else if(i == last) {
                args.add(c.toArgArray());
                jdbcTemplate.batchUpdate(sql, args);
                LOGGER.info("batchInsert, index {}", i);
            }

            if(i != 0 && i % batchSize == 0) {
                args = new ArrayList<>(batchSize);
            }

            args.add(c.toArgArray());
        }
        return size;
    }

    /**
     * 可批量插入接口
     */
    public static interface BatchInsertion {

        /** javaBean转换为insert value对应的参数数组 */
        Object[] toArgArray();
    }
}
