package com.tedrain.mybatis.handler;

import com.tedrain.mybatis.config.MappedStatement;

import java.sql.ResultSet;
import java.util.List;

public interface ResultSetHandler {
    void handleResultSet(ResultSet rs, MappedStatement mappedStatement, List<Object> results) throws Exception;
}
