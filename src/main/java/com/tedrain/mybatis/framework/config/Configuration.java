package com.tedrain.mybatis.framework.config;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 用于映射配置文件数据
 */
public class Configuration {

    /**
     * 数据库配置节点信息
     */
    private DataSource dataSource;

    /**
     * 维护了mapper.xml中的<select|update|delete|insert>节点的封装
     */
    private Map<String, MappedStatement> mappedStatementMap = new HashMap<String, MappedStatement>();

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public MappedStatement getMappedStatementById(String statementId) {
        return mappedStatementMap.get(statementId);
    }

    public void addMappedStatement(String statementId, MappedStatement mappedStatement) {
        this.mappedStatementMap.put(statementId, mappedStatement);
    }
}
