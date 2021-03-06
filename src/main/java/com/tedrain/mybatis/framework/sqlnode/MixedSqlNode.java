package com.tedrain.mybatis.framework.sqlnode;

import com.tedrain.mybatis.framework.sqlnode.iface.SqlNode;

import java.util.List;

/**
 * 组装所有 SqlNode 节点信息
 */
public class MixedSqlNode implements SqlNode {

    private List<SqlNode> sqlNodes;

    public MixedSqlNode(List<SqlNode> sqlNodes) {
        sqlNodes = sqlNodes;
    }

    public void apply(DynamicContext context) {
        for (SqlNode sqlNode : sqlNodes) {
            sqlNode.apply(context);
        }
    }
}
