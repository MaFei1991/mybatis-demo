package com.tedrain.mybatis.config;

import com.tedrain.mybatis.sqlsource.iface.SqlSource;

public class MappedStatement {
    private String statementId;

    private String resultType;

    private Class resultClass;

    private String statementType;

    private SqlSource sqlSource;

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

    public String getStatementType() {
        return statementType;
    }

    public void setStatementType(String statementType) {
        this.statementType = statementType;
    }

    public SqlSource getSqlSource() {
        return sqlSource;
    }

    public void setSqlSource(SqlSource sqlSource) {
        this.sqlSource = sqlSource;
    }

    public MappedStatement(String statementId, Class resultClass, String statementType, SqlSource sqlSource) {
        this.statementId = statementId;
        this.resultClass = resultClass;
        this.statementType = statementType;
        this.sqlSource = sqlSource;
    }

    public Class getResultClass() {
        return resultClass;
    }

    public void setResultClass(Class resultClass) {
        this.resultClass = resultClass;
    }
}
