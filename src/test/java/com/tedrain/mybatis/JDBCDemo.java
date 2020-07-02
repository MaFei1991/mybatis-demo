package com.tedrain.mybatis;

import org.junit.Test;

import java.sql.*;

/**
 * 原生的jdbc存在几个问题：
 * 1. 存在硬编码问题，获取连接、执行statement
 * 2. 频繁创建连接和销毁连接对象，浪费资源
 */
public class JDBCDemo {

    @Test
    public void test() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            // 1. 加载数据库驱动 注册驱动 告知JVM使用的是哪一个数据库的驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
            // 2. 通过驱动管理类获取数据库连接 connection = DriveManger
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/tedrain?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC", "root", "1qaz@WSX");

            // 3. 定义 sql 语句, ? 占位符
            String sql = "SELECT * FROM user where username = ?";
            // 4. 获取预处理 PreparedStatement
            preparedStatement = connection.prepareStatement(sql);

            // 4.1 设置参数，参数位置从 1 开始
            preparedStatement.setString(1, "admin");

            // 4.2 向数据库发送sql语句执行查询，查询出结果集
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                System.out.println(resultSet.getString("id") + " " + resultSet.getString("username"));
            }
            // 5 便利结果集
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
