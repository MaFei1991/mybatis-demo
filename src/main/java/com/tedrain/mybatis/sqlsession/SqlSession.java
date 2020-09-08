package com.tedrain.mybatis.sqlsession;

import java.util.List;

public interface SqlSession {
    <T> List<T> selectList(String statementId, Object param) ;

    <T> T selectOne(String statementId, Object param);
}
