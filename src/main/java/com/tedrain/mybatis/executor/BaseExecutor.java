package com.tedrain.mybatis.executor;

import com.tedrain.mybatis.config.Configuration;
import com.tedrain.mybatis.config.MappedStatement;
import com.tedrain.mybatis.sqlsource.iface.SqlSource;
import com.tedrain.mybatis.sqlsource.model.BoundSql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseExecutor implements Executor {
    private Map<String, List> oneLevelCache = new HashMap<String, List>();

    public <T> List<T> query(Configuration configuration, MappedStatement mappedStatement, Object param) {
        SqlSource sqlSource = mappedStatement.getSqlSource();
        BoundSql boundSql = sqlSource.getBoundSql(param);
        String sql = boundSql.getSql();
        // 先查询一级缓存
        List list = this.oneLevelCache.get(sql);
        if (list != null && list.size() > 0) {
            return list;
        }
        // 没有则查询数据库
        list = queryFromDataSource(configuration, mappedStatement, param, boundSql);
        // 将结果放入缓存
        this.oneLevelCache.put(sql, list);
        return list;
    }

    public abstract List queryFromDataSource(Configuration configuration, MappedStatement mappedStatement, Object param, BoundSql boundSql);
}
