package com.gow.orm.mybatis;

import com.alibaba.fastjson.JSONArray;
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
@MappedTypes({JSONArray.class})
@MappedJdbcTypes({JdbcType.VARCHAR})
public class JsonArrayTypeHandler extends BaseTypeHandler<JSONArray> {
    public JsonArrayTypeHandler() {
    }

    public void setNonNullParameter(PreparedStatement ps, int i, JSONArray parameter, JdbcType jdbcType) throws
            SQLException {
        ps.setString(i, String.valueOf(parameter.toJSONString()));
    }

    public JSONArray getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String sqlJson = rs.getString(columnName);
        return null != sqlJson ? JSONArray.parseArray(sqlJson) : null;
    }

    public JSONArray getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String sqlJson = rs.getString(columnIndex);
        return null != sqlJson ? JSONArray.parseArray(sqlJson) : null;
    }

    public JSONArray getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String sqlJson = cs.getString(columnIndex);
        return null != sqlJson ? JSONArray.parseArray(sqlJson) : null;
    }
}