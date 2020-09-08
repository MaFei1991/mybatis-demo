package com.tedrain.mybatis.sqlsource;

import com.tedrain.mybatis.sqlsource.iface.SqlSource;
import com.tedrain.mybatis.sqlsource.model.BoundSql;
import com.tedrain.mybatis.sqlsource.model.ParameterMapping;

import java.util.List;

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
