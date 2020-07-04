package com.tedrain.mybatis.framework.sqlnode;

import com.tedrain.mybatis.framework.sqlnode.iface.SqlNode;

/**
 * 封装不带有 ${} 的sql信息
 */
public class StaticTextSqlNode implements SqlNode {

    /**
     * 封装未解析的 sql 文本信息
     */
    private String sqlText;

    public StaticTextSqlNode(String sqlText) {
        this.sqlText = sqlText;
    }

    public void apply(DynamicContext context) {

    }
}
