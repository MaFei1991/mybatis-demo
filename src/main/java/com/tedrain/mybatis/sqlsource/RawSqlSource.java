package com.tedrain.mybatis.sqlsource;

import com.tedrain.mybatis.sqlnode.DynamicContext;
import com.tedrain.mybatis.sqlnode.iface.SqlNode;
import com.tedrain.mybatis.sqlsource.iface.SqlSource;
import com.tedrain.mybatis.sqlsource.model.BoundSql;
import com.tedrain.mybatis.utils.GenericTokenParser;
import com.tedrain.mybatis.utils.ParameterMappingTokenHandler;

public class RawSqlSource implements SqlSource {

    //其实就是StaticSqlSource
    private SqlSource sqlSource;

    public RawSqlSource(SqlNode rootSqlNode) {
        // 解析#{}
        // 1.处理所有的SqlNode，合并成一条SQL语句（该语句#{}还未处理）
        DynamicContext context = new DynamicContext(null);
        rootSqlNode.apply(context);

        // 合并之后的SQL语句
        // select * from user where id = #{id}
        String sqlText = context.getSql();
        // 2.处理#{}，得到JDBC可以执行的【SQL语句】，以及解析出来的【参数信息集合】


        // 用来处理#{}中的参数
        // 2.1 、将#{}替换为?----字符串处理
        // 2.2 、将#{}里面的参数名称，比如说id，封装成ParameterMapping对象中，并添加到List集合
        ParameterMappingTokenHandler tokenHandler = new ParameterMappingTokenHandler();

        // 用来解析SQL文本中的#{}或者${}
        GenericTokenParser tokenParser = new GenericTokenParser("#{","}",tokenHandler);

        // JDBC可以直接执行的SQL语句
        String sql = tokenParser.parse(sqlText);

        // 3.将得到的SQL语句和参数信息集合，封装到StaticSqlSource里面存储
        sqlSource = new StaticSqlSource(sql,tokenHandler.getParameterMappings());
    }

    public BoundSql getBoundSql(Object param) {
        return null;
    }
}
