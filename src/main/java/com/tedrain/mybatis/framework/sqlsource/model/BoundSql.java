package com.tedrain.mybatis.framework.sqlsource.model;

import java.util.List;

/**
 * 作用：
 * 1. 用户封装解析之后的sql语句；
 * 2. 储存 #{} 参数集合信息
 */
public class BoundSql {
    /**
     * 解析后的sql语句
     */
    private String sql;

    /**
     * ${}解析出来的参数集合
     */
    private List<ParameterMapping> parameterMappings;

    public BoundSql(String sql, List<ParameterMapping> parameterMappings) {
        this.sql = sql;
        this.parameterMappings = parameterMappings;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public List<ParameterMapping> getParameterMappings() {
        return parameterMappings;
    }

    public void setParameterMappings(List<ParameterMapping> parameterMappings) {
        this.parameterMappings = parameterMappings;
    }
}
