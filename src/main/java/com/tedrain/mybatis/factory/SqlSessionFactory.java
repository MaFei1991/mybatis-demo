package com.tedrain.mybatis.factory;

import com.tedrain.mybatis.sqlsession.SqlSession;

public interface SqlSessionFactory {
    SqlSession openSession();
}
