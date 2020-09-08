package com.tedrain.mybatis.sqlsource.model;

public class ParameterMapping {
    // #{}中的参数名称
    private String name;
    // #{}对于的参数的参数类型
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
