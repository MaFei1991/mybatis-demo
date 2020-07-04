package com.tedrain.mybatis.framework.sqlnode;

import java.util.HashMap;
import java.util.Map;

/**
 * 用来封装SqlNode执行过程中需要的参数信息
 * 以及用来拼接SqlNode执行过程中的SQL文本
 */
public class DynamicContext {

    /**
     * 用于拼接sql语句的 StringBuffer对象
     */
    private StringBuffer sb = new StringBuffer();

    /**
     * 用于封装解析 SqlNode 过程中需要的信息
     */
    private Map<String, Object> bindings = new HashMap<String, Object>();

    /**
     * 必须传入参数
     * @param param
     */
    public DynamicContext(Object param) {
        bindings.put("_parameter", param);
    }

    /**
     * 获取最终合并后的sql文本
     *
     * @return
     */
    public String getSql() {
        return sb.toString();
    }

    /**
     * sql 拼接
     *
     * @param sql
     */
    public void appendSql(String sql) {
        this.sb.append(sql);
        this.sb.append(" ");
    }

    public Map<String, Object> getBindings() {
        return bindings;
    }

    public void addBinding(String key, Object value) {
        this.bindings.put(key, value);
    }
}
