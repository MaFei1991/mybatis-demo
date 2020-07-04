package com.tedrain.mybatis.framework.sqlsource.iface;

import com.tedrain.mybatis.framework.sqlsource.model.BoundSql;

/**
 * 提供对封装的sql信息进行处理的操作
 * 处理<insert|update|delete|select />节点信息
 */
public interface SqlSource {
    BoundSql getBoundSql(Object param);
}
