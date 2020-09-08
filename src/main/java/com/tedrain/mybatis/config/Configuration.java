package com.tedrain.mybatis.config;

import com.tedrain.mybatis.executor.CachingExecutor;
import com.tedrain.mybatis.executor.Executor;
import com.tedrain.mybatis.executor.SimpleExecutor;
import com.tedrain.mybatis.handler.*;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public class Configuration {
    private DataSource dataSource;
    private boolean useCache = true;

    private Map<String,MappedStatement> mappedStatementMap = new HashMap<String,MappedStatement>();

    public DataSource getDataSource() {
        return dataSource;
    }
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public  MappedStatement getMappedStatementById(String statementId) {
        return mappedStatementMap.get(statementId);
    }

    public void addMappedStatement(String statementId,MappedStatement mappedStatement) {
        this.mappedStatementMap.put(statementId, mappedStatement);
    }

    // TODO
    public Executor newExecutor(String executorType){
        executorType = executorType == null ? "simple":executorType;
        Executor executor = null;
        if ("simple".equals(executorType)){
            executor = new SimpleExecutor();
        }//else if ()

        // 默认使用二级缓存执行器去执行一遍
        if (useCache){
            executor = new CachingExecutor(executor);
        }

        return executor;
    }

    public StatementHandler newStatementHandler(String statementType){
        if ("prepared".equals(statementType)){
            return new PreparedStatementHandler(this);
        }else if ("simple".equals(statementType)){
            return new SimpleStatementHandler();
        }else if ("callable".equals(statementType)){
            return new CallableStatementHandler();
        }
        return null;
    }

    public ParameterHandler newParameterHandler(){
        return new DefaultParameterHandler();
    }

    public ResultSetHandler newResultSetHandler(){
        return new DefaultResultSetHandler();
    }
}
