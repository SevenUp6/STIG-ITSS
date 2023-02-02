package com.xjrsoft.common.dbmodel;

import cn.hutool.db.sql.SqlExecutor;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.xjrsoft.common.dbmodel.utils.DataSourceUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class DbExecutor {


    public void executeUpdate(DataSource dataSource,  String sql, Object...params) throws SQLException {
        if (dataSource == null) {
            throw new SQLException("数据源不存在！");
        }
        Connection connection = dataSource.getConnection();
        PreparedStatement pst = connection.prepareStatement(sql);
        if (params != null && params.length > 0) {
            for (int i = 0; i < params.length; i++) {
                pst.setObject(i + 1, params[i]);
            }
        }
        pst.executeUpdate();
        pst.close();
        connection.close();
    }
    public void  executeUpdate(String dataSourceId, String sql, Object...params) throws SQLException {
        DataSource dataSource = DataSourceUtil.getDataSource(dataSourceId);
        executeUpdate(dataSource,sql, params);
    }

    public void batchExecute(String dataSourceId, List<SqlParam> sqlParamList, boolean isAutoCommit) throws SQLException {
        DataSource dataSource = DataSourceUtil.getDataSource(dataSourceId);
        if (dataSource == null) {
            throw new SQLException("数据源不存在！");
        }
        Connection connection = dataSource.getConnection();
        connection.setAutoCommit(isAutoCommit);
        for (SqlParam sqlParam: sqlParamList) {
            try {
                PreparedStatement pst = connection.prepareStatement(sqlParam.getSql());
                Object[] params = sqlParam.getPara();
                if (params != null && params.length > 0) {
                    for (int i = 0; i < params.length; i++) {
                        pst.setObject(i + 1, params[i]);
                    }
                }
                pst.executeUpdate();
            } catch (SQLException e) {
                if (!isAutoCommit) {
                    connection.rollback();
                }
                log.error("执行失败："  + sqlParam.getSql(), e);
                connection.close();
                throw new SQLException("执行sql失败： " + sqlParam.getSql(), e);
            }
        }
        if (!isAutoCommit) {
            connection.commit();
        }

        connection.close();
    }

    public void batchExecuteNonParam(String dbLinkId, String...sqls) throws SQLException {
        DataSource dataSource = DataSourceUtil.getDataSource(dbLinkId);
        SqlExecutor.executeBatch(dataSource.getConnection(), sqls);
    }

    public List<Map<String, Object>> executeQuery(String dataSourceId, String sql, Object...params) throws SQLException {
        DataSource dataSource = DataSourceUtil.getDataSource(dataSourceId);
        if (dataSource == null) {
            throw new SQLException("数据源不存在！id：" + dataSourceId);
        }
        Connection connection = dataSource.getConnection();
        PreparedStatement pst = connection.prepareStatement(sql);
        if (params != null && params.length > 0) {
            for (int i = 0; i < params.length; i++) {
                pst.setObject(i + 1, params[i]);
            }
        }
        ResultSet rs = pst.executeQuery();

        List<Map<String, Object>> maps = buildQueryResultData(rs);

        pst.close();
        connection.close();
        return maps;
    }

    public List<Map<String, Object>> executeQuery(DataSource dataSource, String sql, Object...params) throws SQLException {
        if (dataSource == null) {
            throw new SQLException("数据源不存在！");
        }
        Connection connection = dataSource.getConnection();
        PreparedStatement pst = connection.prepareStatement(sql);
        if (params != null && params.length > 0) {
            for (int i = 0; i < params.length; i++) {
                pst.setObject(i + 1, params[i]);
            }
        }
        ResultSet rs = pst.executeQuery();

        List<Map<String, Object>> maps = buildQueryResultData(rs);

        pst.close();
        connection.close();
        return maps;
    }

    public List<Map<String, Object>> buildQueryResultData(ResultSet rs) throws SQLException {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        String[] labelNames = new String[columnCount + 1];
        int[] types = new int[columnCount + 1];
        buildLabelNamesAndTypes(rsmd, labelNames, types);
        while (rs.next()) {
            Map<String, Object> record = new HashMap<String, Object>();
            for (int i=1; i<=columnCount; i++) {
                Object value;
                if (types[i] < Types.BLOB) {
                    value = rs.getObject(i);
                } else {
                    if (types[i] == Types.CLOB) {
                        value = handleClob(rs.getClob(i));
                    } else if (types[i] == Types.NCLOB) {
                        value = handleClob(rs.getNClob(i));
                    } else if (types[i] == Types.BLOB) {
                        value = handleBlob(rs.getBlob(i));
                    } else {
                        value = rs.getObject(i);
                    }
                }

                record.put(labelNames[i], value);
            }
            result.add(record);
        }
        return result;
    }

    public void buildLabelNamesAndTypes(ResultSetMetaData rsmd, String[] labelNames, int[] types) throws SQLException {
        for (int i=1; i<labelNames.length; i++) {
            labelNames[i] = rsmd.getColumnLabel(i);
            types[i] = rsmd.getColumnType(i);
        }
    }

    public String handleClob(Clob clob) throws SQLException {
        if (clob == null)
            return null;
        Reader reader = null;
        try {
            reader = clob.getCharacterStream();
            if (reader == null)
                return null;
            char[] buffer = new char[(int)clob.length()];
            if (buffer.length == 0)
                return null;
            reader.read(buffer);
            return new String(buffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        finally {
            if (reader != null)
                try {reader.close();} catch (IOException e) {throw new RuntimeException(e);}
        }
    }

    public byte[] handleBlob(Blob blob) throws SQLException {
        if (blob == null)
            return null;
        InputStream is = null;
        try {
            is = blob.getBinaryStream();
            if (is == null)
                return null;
            byte[] data = new byte[(int)blob.length()];
            if (data.length == 0)
                return null;
            is.read(data);
            return data;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        finally {
            if (is != null)
                try {is.close();} catch (IOException e) {throw new RuntimeException(e);}
        }
    }
}
