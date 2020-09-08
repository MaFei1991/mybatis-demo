package com.tedrain.mybatis.utils;

public class ReflectUtils {
    public static Class resolveType(String className){
        try{
            return Class.forName(className);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
