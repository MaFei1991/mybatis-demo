package com.tedrain.mybatis.sqlnode.iface;

import com.tedrain.mybatis.sqlnode.DynamicContext;

public interface SqlNode {
    void apply(DynamicContext context);
}
