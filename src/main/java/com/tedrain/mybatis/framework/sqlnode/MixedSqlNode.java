package com.tedrain.mybatis.framework.sqlnode;

import com.tedrain.mybatis.framework.sqlnode.iface.SqlNode;

import java.util.List;

/**
 *  组装所有 SqlNode 节点信息
 */
public class MixedSqlNode  implements SqlNode {

    private List<SqlNode> SqlNodes;

    public MixedSqlNode(List<SqlNode> sqlNodes) {
        SqlNodes = sqlNodes;
    }

    public void apply(DynamicContext context) {

    }
}
