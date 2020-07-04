package com.tedrain.mybatis.framework.sqlsource.model;

/**
 * 封装#{}解析出来的参数
 */
public class ParameterMapping {
    /**
     * #{}中的参数名称
     */
    private String name;

    /**
     * #{} 中的参数类型
     */
    private Class type;

    public ParameterMapping(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }
}
