package com.tedrain.mybatis.framework.sqlnode;

import com.tedrain.mybatis.framework.sqlnode.iface.SqlNode;

/**
 * 封装<if /> 标签的sql信息
 */
public class IfSqlNode implements SqlNode {

    /**
     * <if test=""></if> test 属性
     */
    private String test;

    /**
     * <if/> 的子标签
     */
    private SqlNode rootSqlNode;

    public IfSqlNode(String test, SqlNode rootSqlNode) {
        this.test = test;
        this.rootSqlNode = rootSqlNode;
    }

    public void apply(DynamicContext context) {

    }
}
