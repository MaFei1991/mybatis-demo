package com.tedrain.mybatis.executor;

import com.tedrain.mybatis.config.Configuration;
import com.tedrain.mybatis.config.MappedStatement;
import com.tedrain.mybatis.sqlsource.model.BoundSql;

import java.util.List;

public class CachingExecutor implements Executor {
    private Executor executor;

    public CachingExecutor(Executor executor) {
        this.executor = executor;
    }

    public <T> List<T> query(Configuration configuration,MappedStatement mappedStatement, Object param) {
        //查询二级缓存

        // 二级缓存没有，则走一级缓存逻辑
        return executor.query(configuration,mappedStatement,param);
    }
}
