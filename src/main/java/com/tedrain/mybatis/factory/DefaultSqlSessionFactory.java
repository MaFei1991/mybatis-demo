package com.tedrain.mybatis.factory;

import com.tedrain.mybatis.config.Configuration;
import com.tedrain.mybatis.sqlsession.DefaultSqlSession;
import com.tedrain.mybatis.sqlsession.SqlSession;

public class DefaultSqlSessionFactory implements SqlSessionFactory {
    private Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    public SqlSession openSession() {
        return new DefaultSqlSession(configuration);
    }
}
