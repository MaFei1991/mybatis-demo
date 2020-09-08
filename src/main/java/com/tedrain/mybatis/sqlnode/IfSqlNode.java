package com.tedrain.mybatis.sqlnode;

import com.tedrain.mybatis.sqlnode.iface.SqlNode;
import com.tedrain.mybatis.utils.OgnlUtils;

public class IfSqlNode implements SqlNode {
    // if标签的test属性(获取OGNL表达式)
    private String test;

    // if标签的子标签集合
    private SqlNode rootSqlNode;

    public IfSqlNode(String test, SqlNode rootSqlNode) {
        this.test = test;
        this.rootSqlNode = rootSqlNode;
    }

    public void apply(DynamicContext context) {
        Object parameter = context.getBindings().get("_parameter");
        boolean evaluateBoolean = OgnlUtils.evaluateBoolean(test, parameter);
        if (evaluateBoolean){
            // 递归去解析子节点
            rootSqlNode.apply(context);
        }
    }
}
