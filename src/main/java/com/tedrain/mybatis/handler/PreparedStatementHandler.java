package com.tedrain.mybatis.handler;

import com.tedrain.mybatis.config.Configuration;
import com.tedrain.mybatis.config.MappedStatement;
import com.tedrain.mybatis.sqlsource.model.BoundSql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

public class PreparedStatementHandler implements StatementHandler {
    private ParameterHandler parameterHandler;

    private ResultSetHandler resultSetHandler;

    public PreparedStatementHandler(Configuration configuration) {
        parameterHandler = configuration.newParameterHandler();
        resultSetHandler = configuration.newResultSetHandler();
    }


    public Statement prepared(Connection connection, String sql) throws Exception{
        return connection.prepareStatement(sql);
    }


    public void setParameters(Statement statement, Object param, BoundSql boundSql)throws Exception {
        PreparedStatement preparedStatement = (PreparedStatement) statement;
        parameterHandler.setParameters(preparedStatement, param, boundSql);
    }


    public void query(Statement statement, MappedStatement mappedStatement, List<Object> results)throws Exception {
        PreparedStatement preparedStatement = (PreparedStatement) statement;
        ResultSet rs = preparedStatement.executeQuery();
        resultSetHandler.handleResultSet(rs,mappedStatement,results);
    }
}
