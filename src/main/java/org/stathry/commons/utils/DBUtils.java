package org.stathry.commons.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.stathry.commons.excel.ExcelReading;
import org.stathry.commons.excel.ExcelWriting;
import org.stathry.commons.model.dto.TableRange;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * 数据库操作工具类
 * Created by dongdaiming on 2018-09-29 14:58
 */
public class DBUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(DBUtils.class);

    private static final JdbcTemplate jdbcTemplate;
    private static final int batchSize = 200;
    private static final String schema = ConfigManager.get("jdbc", "jdbc.schema");

    static {
        jdbcTemplate = ApplicationContextUtils.getBean("jdbcTemplate", JdbcTemplate.class);
    }

    private DBUtils() {
    }

    public static <K extends Comparable<K>> List<Map<String, Object>> listData(String table, TableRange<K> area) {
        checkListArea(table, area);
        StringBuilder sql = new StringBuilder("select * from ").append(table);
        List<Object> args = new ArrayList<>(8);
        if (area.getBeginKey() != null && area.getEndKey() != null) {
            sql.append(" where ").append(area.getPrimaryKey()).append(" between ? and ? ");
            args.add(area.getBeginKey());
            args.add(area.getEndKey());
        } else if (area.getBeginKey() != null && area.getEndKey() == null) {
            sql.append(" where ").append(area.getPrimaryKey()).append(" >= ? ");
            args.add(area.getBeginKey());
        } else if (area.getBeginKey() == null && area.getEndKey() != null) {
            sql.append(" where ").append(area.getPrimaryKey()).append(" <= ? ");
            args.add(area.getEndKey());
        }

        if (area.getBeginTime() != null && area.getEndTime() != null) {
            whereOrAnd(sql);
            sql.append(area.getTimeColumn()).append(" between ? and ? ");
            args.add(area.getBeginTime());
            args.add(area.getEndTime());
        } else if (area.getBeginTime() != null && area.getEndTime() == null) {
            whereOrAnd(sql);
            sql.append(area.getTimeColumn()).append(" >= ? ");
            args.add(area.getBeginTime());
        } else if (area.getBeginTime() == null && area.getEndTime() != null) {
            whereOrAnd(sql);
            sql.append(area.getTimeColumn()).append(" <= ? ");
            args.add(area.getEndTime());
        }

        sql.append(" limit ? ");
        args.add(area.getLimit());
        return jdbcTemplate.queryForList(sql.toString(), args.toArray());
    }

    private static void whereOrAnd(StringBuilder sql) {
        if (!sql.toString().contains("where")) {
            sql.append(" where ");
        } else {
            sql.append(" and ");
        }
    }

    private static <K extends Comparable<K>> void checkListArea(String table, TableRange area) {
        if (area == null || StringUtils.isBlank(area.getPrimaryKey()) || StringUtils.isBlank(table)) {
            throw new IllegalArgumentException("required primaryKey.");
        }
        if (area.getLimit() < 0 || (area.getBeginKey() != null && area.getEndKey() != null && area.getEndKey().compareTo(area.getBeginKey()) < 0)) {
            throw new IllegalArgumentException("illegal limit, beginKey, or endKey.");
        } else if (StringUtils.isNotBlank(area.getTimeColumn()) && area.getBeginTime() != null
                && area.getEndTime() != null && area.getEndTime().before(area.getBeginTime())) {
            throw new IllegalArgumentException("illegal beginTime, or endTime.");
        }
    }

    public static int exportToExcel(String path, String table, TableRange area) {
        List<String> header = queryColumns(table);
        List<Map<String, Object>> list = listData(table, area);
        ExcelWriting.writeLinkedMaps(path, list, header);
        return list.size();
    }

    private static List<String> queryColumns(String table) {
        return jdbcTemplate.queryForList("select column_name from INFORMATION_SCHEMA.Columns where table_name=? and table_schema=? ", String.class, table, schema);
    }

    /**
     * 从excel读取数据并批量导入至mysql
     *
     * @param path
     * @param table
     * @return
     */
    public static int insertFromExcel(String path, String table) {
        List<Map<String, String>> list = ExcelReading.readToMaps(path);
        int size = list == null ? 0 : list.size();

        LOGGER.info("read data from excel, size {}.", size);
        if (size == 0) {
            return 0;
        }

        Set<String> cols = new TreeSet<>(list.get(0).keySet());

        String sql = keysToSQL(cols, table);

        LOGGER.info("generate sql by excel header, sql:{}", sql);

        int bs = batchSize < size ? batchSize : size;
        List<Object[]> args = new ArrayList<>(bs);
        Map<String, String> r;
        for (int i = 0, last = size - 1; i < size; i++) {
            r = list.get(i);
            if ((i != 0 && i % bs == 0)) {
                LOGGER.info("batch insert data to table {}, index {}, rows {}.", table, i, args.size());
                jdbcTemplate.batchUpdate(sql, args);
            } else if (i == last) {
                args.add(mapToArgArray(r, cols));
                LOGGER.info("batch insert data to table {}, index {}, rows {}.", table, i, args.size());
                jdbcTemplate.batchUpdate(sql, args);
            }

            if (i != 0 && i % bs == 0) {
                args = new ArrayList<>(bs);
            }

            args.add(mapToArgArray(r, cols));
        }
        return size;
    }

    private static Object[] mapToArgArray(Map<String, String> map, Set<String> cols) {
        int len = cols.size();
        Object[] args = new Object[len];
        Iterator<String> it = cols.iterator();
        String col;
        for (int i = 0; it.hasNext(); i++) {
            col = it.next();
            args[i] = map.get(col);
        }
        return args;
    }

    private static String keysToSQL(Set<String> cols, String table) {
        StringBuilder sql1 = new StringBuilder("INSERT INTO ").append(table).append('(');
        StringBuilder sql2 = new StringBuilder("VALUES(");
        for (String c : cols) {
            sql1.append(c).append(',');
            sql2.append("?,");
        }
        sql1.deleteCharAt(sql1.length() - 1);
        sql1.append(')');
        sql2.deleteCharAt(sql2.length() - 1);
        sql2.append(')');
        return sql1.append(' ').append(sql2).toString();
    }
}
