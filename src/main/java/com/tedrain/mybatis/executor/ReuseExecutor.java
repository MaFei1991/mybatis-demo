package com.tedrain.mybatis.executor;

import com.tedrain.mybatis.config.Configuration;
import com.tedrain.mybatis.config.MappedStatement;
import com.tedrain.mybatis.sqlsource.model.BoundSql;

import java.util.List;

public class ReuseExecutor extends BaseExecutor {
    public List queryFromDataSource(Configuration configuration, MappedStatement mappedStatement, Object param, BoundSql boundSql) {
        return null;
    }
}
