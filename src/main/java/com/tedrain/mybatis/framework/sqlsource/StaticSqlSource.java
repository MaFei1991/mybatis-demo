package com.tedrain.mybatis.framework.sqlsource;

import com.tedrain.mybatis.framework.sqlsource.iface.SqlSource;
import com.tedrain.mybatis.framework.sqlsource.model.BoundSql;
import com.tedrain.mybatis.framework.sqlsource.model.ParameterMapping;

import java.util.List;

/**
 * 只需要封装已经解析出来的SqlSource信息
 */
public class StaticSqlSource implements SqlSource {
    private String sql;

    private List<ParameterMapping> parameterMappings;

    public StaticSqlSource(String sql, List<ParameterMapping> parameterMappings) {
        this.sql = sql;
        this.parameterMappings = parameterMappings;
    }

    public BoundSql getBoundSql(Object param) {
        return new BoundSql(sql, parameterMappings);
    }
}
