package com.tedrain.mybatis.framework.sqlnode.iface;

import com.tedrain.mybatis.framework.sqlnode.DynamicContext;

/**
 * 提供对 sql 节点信息的处理功能
 */
public interface SqlNode {

    void apply(DynamicContext context);
}
