package com.tedrain.mybatis.handler;

import com.tedrain.mybatis.sqlsource.model.BoundSql;

import java.sql.Statement;

public interface ParameterHandler {
    void setParameters(Statement statement, Object param, BoundSql boundSql)throws Exception;
}
