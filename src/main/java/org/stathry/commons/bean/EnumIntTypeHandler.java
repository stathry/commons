package org.stathry.commons.bean;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * TODO
 * Created by dongdaiming on 2018-11-29 17:09
 */
public class EnumIntTypeHandler extends BaseTypeHandler<Integer> implements TypeHandler<Integer> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Integer parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, parameter);
    }

    @Override
    public Integer getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return rs.getInt(columnName);
    }

    @Override
    public Integer getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getInt(columnIndex);
    }

    @Override
    public Integer getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return cs.getInt(columnIndex);
    }
}
