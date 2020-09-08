package com.tedrain.mybatis.handler;

import com.tedrain.mybatis.config.MappedStatement;
import com.tedrain.mybatis.sqlsource.model.BoundSql;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

public class SimpleStatementHandler implements StatementHandler {
    public Statement prepared(Connection connection, String sql) throws Exception {
        return null;
    }

    public void setParameters(Statement statement, Object param, BoundSql boundSql) throws Exception {

    }

    public void query(Statement statement, MappedStatement mappedStatement, List<Object> results) throws Exception {

    }
}
