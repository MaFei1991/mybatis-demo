package com.tedrain.mybatis.executor;

import com.tedrain.mybatis.config.Configuration;
import com.tedrain.mybatis.config.MappedStatement;
import com.tedrain.mybatis.handler.StatementHandler;
import com.tedrain.mybatis.sqlsource.iface.SqlSource;
import com.tedrain.mybatis.sqlsource.model.BoundSql;
import com.tedrain.mybatis.sqlsource.model.ParameterMapping;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SimpleExecutor extends BaseExecutor {
    public List queryFromDataSource(Configuration configuration, MappedStatement mappedStatement, Object param, BoundSql boundSql) {
        List<Object> results = new ArrayList<Object>();

        Connection connection = null;
        Statement statement = null;

        try {
            // 1.获取连接
            connection = getConnection(configuration);

            // 2.获取SQL语句(SqlSource和SqlNode的执行过程)
            String sql = boundSql.getSql();

            StatementHandler statementHandler = configuration.newStatementHandler(mappedStatement.getStatementType());
            statement = statementHandler.prepared(connection,sql);
            // 3.创建Statement对象
            // statement = createStatement(connection,sql,mappedStatement);

            // 4.设置参数
            statementHandler.setParameters(statement,param,boundSql);

            // 5.执行Statement
            statementHandler.query(statement,mappedStatement,results);

            // 6.处理ResultSet
            // handleResultSet(rs,results,mappedStatement);
            return results;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 释放资源
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
        return  null;
    }

    /**
     * 处理结果集
     * @param rs
     * @param results
     * @param mappedStatement
     * @param <T>
     * @throws Exception
     */
    private <T> void handleResultSet(ResultSet rs, List<T> results, MappedStatement mappedStatement) throws Exception{
        // 遍历查询结果集
        Class clazz= mappedStatement.getResultClass();

        Object result = null;
        while (rs.next()) {
            result = clazz.newInstance();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                // 取出resultset中的每一行结果中的列的名称
                String columnName = metaData.getColumnName(i);
                // 要求列的名称和映射 对象的属性名称要一致
                Field field = clazz.getDeclaredField(columnName);
                // 暴力访问私有成员
                field.setAccessible(true);
                field.set(result,rs.getObject(i));
            }

            results.add((T) result);
        }
    }

    /**
     * 执行Statement
     * @param statement
     * @return
     * @throws Exception
     */
    private ResultSet handleStatement(Statement statement) throws Exception{
        if (statement instanceof PreparedStatement) {
            PreparedStatement preparedStatement = (PreparedStatement) statement;
            return preparedStatement.executeQuery();
        }
        return null;
    }

    /**
     * 设置参数
     * @param statement
     * @param param
     * @param boundSql
     * @throws Exception
     */
    private void setParameters(Statement statement, Object param, BoundSql boundSql) throws Exception{
        if (statement instanceof PreparedStatement){
            PreparedStatement preparedStatement = (PreparedStatement) statement;
            if (param instanceof Integer || param instanceof String){
                preparedStatement.setObject(1, param);
            }else if (param instanceof Map){
                Map paramMap = (Map) param;
                // TODO 结合#{}的处理逻辑进行改造
                List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
                for (int i = 0 ; i<parameterMappings.size() ;i++) {
                    ParameterMapping parameterMapping = parameterMappings.get(i);
                    String name = parameterMapping.getName();
                    // 获取到的参数
                    Object value = paramMap.get(name);
                    // 获取到的value对应的Java类型
                    Class type = parameterMapping.getType();
                    if (type != null){
                        //TODO
                        // preparedStatement.setInt(i+1, value);
                        // preparedStatement.setString(i+1, value);

                    }else{
                        preparedStatement.setObject(i+1, value);
                    }
                }
            }else{
                //TODO
            }
        }


    }

    private BoundSql getSql(MappedStatement mappedStatement, Object param) {
        SqlSource sqlSource = mappedStatement.getSqlSource();
        BoundSql boundSql = sqlSource.getBoundSql(param);
        return boundSql;
    }

    /**
     * 获取连接的代码
     * @return
     */
    private Connection getConnection(Configuration configuration){
        // 优化连接处理
        try {
            DataSource dataSource = configuration.getDataSource();
            return dataSource.getConnection();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 创建Statement对象
     * @param connection
     * @param sql
     * @param mappedStatement
     * @return
     * @throws Exception
     */
    private Statement createStatement(Connection connection, String sql, MappedStatement mappedStatement) throws Exception{
        String statementType = mappedStatement.getStatementType();
        if (statementType.equals("prepared")){
            return connection.prepareStatement(sql);
        }else if (statementType.equals("statement")){
            return connection.createStatement();
        }else if (statementType.equals("callable")){
            //TODO
        }else{
            //TODO
        }
        return null;
    }
}
