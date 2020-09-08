package com.tedrain.mybatis.executor;

import com.tedrain.mybatis.config.Configuration;
import com.tedrain.mybatis.config.MappedStatement;

import java.util.List;

public interface Executor {
    <T> List<T> query(Configuration configuration, MappedStatement mappedStatement, Object param) ;
}
