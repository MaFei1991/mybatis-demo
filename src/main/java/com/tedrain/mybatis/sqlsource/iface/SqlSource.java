package com.tedrain.mybatis.sqlsource.iface;

import com.tedrain.mybatis.sqlsource.model.BoundSql;

public interface SqlSource {
    BoundSql getBoundSql(Object param);
}
