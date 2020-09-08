package com.tedrain.mybatis.sqlnode;

import com.tedrain.mybatis.sqlnode.iface.SqlNode;

public class StaticTextSqlNode implements SqlNode {
    // 封装未解析的sql文本信息
    private String sqlText;

    public StaticTextSqlNode(String sqlText) {
        this.sqlText = sqlText;
    }

    public void apply(DynamicContext context) {
        context.appendSql(sqlText);
    }
}
