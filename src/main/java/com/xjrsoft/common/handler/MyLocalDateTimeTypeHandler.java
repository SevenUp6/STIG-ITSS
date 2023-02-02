package com.xjrsoft.common.handler;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.LocalDateTimeTypeHandler;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class MyLocalDateTimeTypeHandler extends LocalDateTimeTypeHandler {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, LocalDateTime parameter, JdbcType jdbcType)
            throws SQLException {
        ps.setObject(i, Timestamp.valueOf(parameter));
    }

    @Override
    public LocalDateTime getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Object object = rs.getObject(columnName);
        return getLocalDateTime(object);
    }

    @Override
    public LocalDateTime getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        Object object = rs.getObject(columnIndex);
        return getLocalDateTime(object);
    }

    @Override
    public LocalDateTime getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        Object object = cs.getObject(columnIndex);
        return getLocalDateTime(object);
    }

    private static LocalDateTime getLocalDateTime(Object object) {
        if(object instanceof java.sql.Timestamp){//在这里强行转换，将sql的时间转换为LocalDateTime
            return ((Timestamp)object).toLocalDateTime();
        }
        return null;
    }
}
