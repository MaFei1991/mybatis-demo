package com.tedrain.mybatis.framework.sqlsource;

import com.tedrain.mybatis.framework.sqlnode.iface.SqlNode;
import com.tedrain.mybatis.framework.sqlsource.iface.SqlSource;
import com.tedrain.mybatis.framework.sqlsource.model.BoundSql;

/**
 * 封装的是带有${}或者动态SQL标签的信息
 * 注意：${}需要每次执行SQL语句的时候，都要解析。
 * 也就是说每次调用getBoundSql方法的时候，去解析SqlNode
 */
public class DynamicSqlSource implements SqlSource {

    private SqlNode rootSqlNode;

    public DynamicSqlSource(SqlNode rootSqlNode) {
        this.rootSqlNode = rootSqlNode;
    }

    public BoundSql getBoundSql(Object param) {
        return null;
    }
}
