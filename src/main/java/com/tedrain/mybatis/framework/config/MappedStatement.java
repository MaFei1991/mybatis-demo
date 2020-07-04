package com.tedrain.mybatis.framework.config;

import com.tedrain.mybatis.framework.sqlsource.iface.SqlSource;

/**
 * MappedStatement维护了一条<select|update|delete|insert>节点的封装
 */
public class MappedStatement {

    private String statementId;

    private String resultType;

    private Class resultClass;

    private String statementType;

    private SqlSource sqlsource;

    public MappedStatement(String statementId, Class resultClass, String statementType, SqlSource sqlsource) {
        this.statementId = statementId;
        this.resultClass = resultClass;
        this.statementType = statementType;
        this.sqlsource = sqlsource;
    }

    public String getStatementId() {
        return statementId;
    }

    public void setStatementId(String statementId) {
        this.statementId = statementId;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public Class getResultClass() {
        return resultClass;
    }

    public void setResultClass(Class resultClass) {
        this.resultClass = resultClass;
    }

    public String getStatementType() {
        return statementType;
    }

    public void setStatementType(String statementType) {
        this.statementType = statementType;
    }

    public SqlSource getSqlsource() {
        return sqlsource;
    }

    public void setSqlsource(SqlSource sqlsource) {
        this.sqlsource = sqlsource;
    }
}
