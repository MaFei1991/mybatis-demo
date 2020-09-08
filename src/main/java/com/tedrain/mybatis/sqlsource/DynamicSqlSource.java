package com.tedrain.mybatis.sqlsource;

import com.tedrain.mybatis.sqlnode.iface.SqlNode;
import com.tedrain.mybatis.sqlsource.iface.SqlSource;
import com.tedrain.mybatis.sqlsource.model.BoundSql;

public class DynamicSqlSource implements SqlSource {
    private SqlNode rootSqlNode;

    public DynamicSqlSource(SqlNode rootSqlNode) {
        this.rootSqlNode = rootSqlNode;
    }

    public BoundSql getBoundSql(Object param) {
        return null;
    }
}
