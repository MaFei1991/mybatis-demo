package com.tedrain.mybatis.framework.sqlsource;

import com.tedrain.mybatis.framework.sqlnode.DynamicContext;
import com.tedrain.mybatis.framework.sqlnode.iface.SqlNode;
import com.tedrain.mybatis.framework.sqlsource.iface.SqlSource;
import com.tedrain.mybatis.framework.sqlsource.model.BoundSql;
import com.tedrain.mybatis.framework.utils.GenericTokenParser;
import com.tedrain.mybatis.framework.utils.ParameterMappingTokenHandler;

/**
 * 封装的是不带有${}或者动态SQL标签的信息，比如#{}
 * 注意：#{}只需要解析一次就可以了。
 * 也就是说不能每次调用getBoundSql方法的时候，去解析SqlNode
 * 只能在构造方法中去解析SqlNode
 */
public class RawSqlSource implements SqlSource {

    private SqlSource sqlSource;

    public RawSqlSource(SqlNode rootSqlNode) {
        // 解析#{}
        // 1. 处理所有的 sqlnode, 合并成一个调 sql 语句 （该语句#{}还未处理）
        DynamicContext context = new DynamicContext(null);
        rootSqlNode.apply(context);

        // 2. 处理 #{}，得到JDBC可以执行的【sql语句】，以解析出来参数信息结合
        // 将 #{} 替换为? ---- 字符串处理
        // 将 #{}里面的参数名称，比如说id,封装到parameterMapping对象中，并添加到 List 集合
        String sqlText = context.getSql();
        ParameterMappingTokenHandler tokenHandler = new ParameterMappingTokenHandler();
        GenericTokenParser tokenParser = new GenericTokenParser("#{", "}", tokenHandler);
        // JDBC 可以直接执行的 sql
        String sql = tokenParser.parse(sqlText);

        // 3. 将得到的 sql语句和参数信息结合，封装到 staticSqlSource里面存储
        sqlSource = new StaticSqlSource(sql, tokenHandler.getParameterMappings());
    }

    public BoundSql getBoundSql(Object param) {
        return sqlSource.getBoundSql(param);
    }
}
