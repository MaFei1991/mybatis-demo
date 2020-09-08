package com.tedrain.mybatis.framework.sqlsource;

import com.tedrain.mybatis.framework.sqlnode.DynamicContext;
import com.tedrain.mybatis.framework.sqlnode.iface.SqlNode;
import com.tedrain.mybatis.framework.sqlsource.iface.SqlSource;
import com.tedrain.mybatis.framework.sqlsource.model.BoundSql;
import com.tedrain.mybatis.framework.utils.GenericTokenParser;
import com.tedrain.mybatis.framework.utils.ParameterMappingTokenHandler;

/**
 * 封装的是带有${}或者动态SQL标签的信息
 * 注意：${}需要每次执行SQL语句的时候，都要解析。
 * 也就是说每次调用getBoundSql方法的时候，去解析SqlNode
 */
public class DynamicSqlSource implements SqlSource {

    private SqlNode rootSqlNode;

    public DynamicSqlSource(SqlNode rootSqlNode) {
        this.rootSqlNode = rootSqlNode;
    }

    public BoundSql getBoundSql(Object param) {
        // 1.处理所有的SqlNode，合并成一条SQL语句（该语句${}已经处理，而#{}还未处理）
        DynamicContext context = new DynamicContext(param);
        rootSqlNode.apply(context);
        // 合并之后的SQL语句
        // select * from user where id = #{id}
        String sqlText = context.getSql();
        System.out.println("#{}未处理的SQL语句：" + sqlText);

        // 2.处理#{}，得到JDBC可以执行的【SQL语句】，以及解析出来的【参数信息集合】

        // 用来处理#{}中的参数
        // 2.1 、将#{}替换为?----字符串处理
        // 2.2 、将#{}里面的参数名称，比如说id，封装成ParameterMapping对象中，并添加到List集合
        ParameterMappingTokenHandler tokenHandler = new ParameterMappingTokenHandler();
        // 用来解析SQL文本中的#{}或者${}
        GenericTokenParser tokenParser = new GenericTokenParser("#{", "}", tokenHandler);
        // JDBC可以直接执行的SQL语句
        String sql = tokenParser.parse(sqlText);
        System.out.println("#{}处理之后的SQL语句：" + sql);
        // 3.将得到的SQL语句和参数信息集合，封装到StaticSqlSource里面存储
        return new BoundSql(sql, tokenHandler.getParameterMappings());
    }
}
