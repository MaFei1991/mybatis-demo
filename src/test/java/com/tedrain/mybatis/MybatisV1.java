package com.tedrain.mybatis;

import com.tedrain.mybatis.po.User;
import org.apache.commons.dbcp.BasicDataSource;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;


/**
 * V1 版本总结：
 * 1. 通过类加载器  this.getClass().getClassLoader().getResourceAsStream("db.properties") 读取配置文件并给数据库连接池BasicDataSource() 赋值
 * 2. 反射，通过反射的方式获取 javabean 字段属性 以及 通过 getMetaData 获取表结构
 * 反射的方式： Class.forName(类全路径)
 * 对象.getClass()
 * 类名.class
 * .newInstance() 实例化一个类
 * 3. 泛型---> 泛型声明 泛型类型
 */
public class MybatisV1 {
    private static BasicDataSource dataSource = null;

    // HashMap<K,V>
    private Properties properties = new Properties();

    @Before
    public void initDataSource() {
        loadProperties("jdbc.properties");

        if (dataSource == null) {
            dataSource = new BasicDataSource();
            assert properties != null;
            dataSource.setDriverClassName(properties.getProperty("db.driver"));
            dataSource.setUrl(properties.getProperty("db.url"));
            dataSource.setUsername(properties.getProperty("db.username"));
            dataSource.setPassword(properties.getProperty("db.password"));
        }
    }

    @Test
    public void test() {
        // 1. 根据用户ID 查询用户信息
        List<User> users1 = selectList("queryUserById", 2);
        System.out.println(users1);
        // 2. 根据用户参数查询用户信息
        Map param = new HashMap();
        param.put("username", "admin");
        param.put("sex", "男");
        System.out.println("===========================================================================");
        List<User> user2 = selectList("queryUserByParams", param);
        System.out.println(user2);

    }

    private <T> List<T> selectList(String statementId, Object param) {
        List<T> results = new ArrayList<T>();
        try {
            // 1. 获取connection连接
            Connection connection = dataSource.getConnection();
            // 2. 获取sql语句
            String sql = properties.getProperty("db.sql." + statementId);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            // 3. 设置参数，发送sql执行查询
            if (param instanceof Integer || param instanceof String) {
                preparedStatement.setObject(1, param);
            } else if (param instanceof Map) {
                Map paramMap = (Map) param;
                String columnNames = properties.getProperty("db.sql." + statementId + ".columnnames");
                String[] names = columnNames.split(",");
                for (int i = 0; i < names.length; i++) {
                    String cloumnName = names[i];
                    Object value = paramMap.get(cloumnName);
                    preparedStatement.setObject(i + 1, value);
                }
            } else {
                // TODO
            }

            ResultSet resultSet = preparedStatement.executeQuery();
            // 遍历查询结果
            String resultClassName = properties.getProperty("db.sql." + statementId + ".resultclassname");
            Class<?> clazz = Class.forName(resultClassName);
            Object result = null;
            while (resultSet.next()) {
                result = clazz.newInstance();
                // getMetaData: 获取表结构
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    // 取出resultset中的每一行结果中的列的名称
                    String columnName = metaData.getColumnName(i);
                    // 要求列的名称和映射 对象的属性名称要一致 getDeclaredFiled 仅能获取类本身的属性成员（包括私有、共有、保护）
                    Field field = clazz.getDeclaredField(columnName);
                    // 暴力访问私有成员
                    field.setAccessible(true);
                    // 向对象的这个Field属性设置新值value
                    field.set(result, resultSet.getObject(i));
                }
                results.add((T) result);
            }
            return results;

        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }

    private void loadProperties(String location) {
        InputStream inputStream = null;
        try {
            inputStream = this.getClass().getClassLoader().getResourceAsStream(location); // ?
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
