package com.tedrain.mybatis.framework.sqlnode;

import com.tedrain.mybatis.framework.sqlnode.iface.SqlNode;

/**
 * 封装带有 ${} 的 sql信息
 */
public class TextSqlNode implements SqlNode {

    /**
     * 封装未解析的sql文本信息
     */
    private String sqlText;

    public TextSqlNode(String sqlText) {
        this.sqlText = sqlText;
    }

    /**
     * 判断封装的sql文本是否带有${}
     * @return
     */
    public boolean isDynamic() {
        return this.sqlText.indexOf("${") > -1;
    }


    public void apply(DynamicContext context) {

    }
}
