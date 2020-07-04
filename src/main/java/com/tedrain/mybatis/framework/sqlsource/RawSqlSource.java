package com.tedrain.mybatis.framework.sqlsource;

import com.tedrain.mybatis.framework.sqlnode.iface.SqlNode;
import com.tedrain.mybatis.framework.sqlsource.iface.SqlSource;
import com.tedrain.mybatis.framework.sqlsource.model.BoundSql;

/**
 * 封装的是不带有${}或者动态SQL标签的信息，比如#{}
 * 注意：#{}只需要解析一次就可以了。
 * 也就是说不能每次调用getBoundSql方法的时候，去解析SqlNode
 * 只能在构造方法中去解析SqlNode
 */
public class RawSqlSource implements SqlSource {

    private SqlSource sqlSource;

    public RawSqlSource(SqlNode rootSqlNode) {
        // TODO 解析#{}
    }

    public BoundSql getBoundSql(Object param) {
        return null;
    }
}
