package com.gow.orm.mybatis;

import com.alibaba.fastjson.JSON;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

/**
 * @author gow
 * @date 2021/7/14
 */
@MappedTypes({JSON.class})
@MappedJdbcTypes({JdbcType.VARCHAR})
public class JsonTypeHandler extends BaseTypeHandler<JSON> {
    public JsonTypeHandler() {
    }

    public void setNonNullParameter(PreparedStatement ps, int i, JSON parameter, JdbcType jdbcType) throws
            SQLException {
        ps.setString(i, String.valueOf(parameter.toJSONString()));
    }

    public JSON getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String sqlJson = rs.getString(columnName);
        return null != sqlJson ? JSON.parseObject(sqlJson) : null;
    }

    public JSON getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String sqlJson = rs.getString(columnIndex);
        return null != sqlJson ? JSON.parseObject(sqlJson) : null;
    }

    public JSON getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String sqlJson = cs.getString(columnIndex);
        return null != sqlJson ? JSON.parseObject(sqlJson) : null;
    }
}
