package com.tedrain.mybatis.sqlnode;

import com.tedrain.mybatis.sqlnode.iface.SqlNode;

import java.util.List;

public class MixedSqlNode implements SqlNode {
    private List<SqlNode> sqlNodes;

    public MixedSqlNode(List<SqlNode> sqlNodes) {
        this.sqlNodes = sqlNodes;
    }

    public void apply(DynamicContext context) {
        for (SqlNode sqlNode : sqlNodes) {
            sqlNode.apply(context);
        }
    }
}
