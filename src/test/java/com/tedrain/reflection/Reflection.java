package com.tedrain.reflection;

import org.junit.Test;

public class Reflection {

    /**
     * 1. 反射：Java 的反射就是程序在运行期可以获取一个对象的所有信息。
     * 2. 获取Class的三种方法
     *    类.class;
     *    实例.getClass()
     *    Class.forName(类全路径)
     */
    @Test
    public void test() {
        Class<String> stringClass = String.class;
        try {

            // 反射实例化对象，智能调用无参构造函数
            String instance = stringClass.newInstance();
            instance = "Hello, world";
            System.out.println(instance);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
