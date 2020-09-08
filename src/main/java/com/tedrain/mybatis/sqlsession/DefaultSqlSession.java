package com.tedrain.mybatis.sqlsession;

import com.tedrain.mybatis.config.Configuration;
import com.tedrain.mybatis.config.MappedStatement;
import com.tedrain.mybatis.executor.Executor;

import java.util.List;

public class DefaultSqlSession implements SqlSession {
    private Configuration configuration;

    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
    }


    public <T> List<T> selectList(String statementId, Object param) {
        MappedStatement mappedStatement = configuration.getMappedStatementById(statementId);
        Executor executor = configuration.newExecutor(null);
        return executor.query(configuration, mappedStatement, param);
    }


    public <T> T selectOne(String statementId, Object param) {
        List<Object> list = this.selectList(statementId, param);
        if (list == null) {
            return null;
        } else if (list.size() == 1) {
            return (T) list.get(0);
        } else if (list.size() != 1) {
            //TODO
        }
        return null;
    }
}
