package com.tedrain.mybatis;

import com.tedrain.mybatis.framework.config.Configuration;
import com.tedrain.mybatis.framework.config.MappedStatement;
import com.tedrain.mybatis.framework.sqlnode.IfSqlNode;
import com.tedrain.mybatis.framework.sqlnode.MixedSqlNode;
import com.tedrain.mybatis.framework.sqlnode.StaticTextSqlNode;
import com.tedrain.mybatis.framework.sqlnode.TextSqlNode;
import com.tedrain.mybatis.framework.sqlnode.iface.SqlNode;
import com.tedrain.mybatis.framework.sqlsource.DynamicSqlSource;
import com.tedrain.mybatis.framework.sqlsource.RawSqlSource;
import com.tedrain.mybatis.framework.sqlsource.iface.SqlSource;
import com.tedrain.mybatis.framework.sqlsource.model.BoundSql;
import com.tedrain.mybatis.framework.sqlsource.model.ParameterMapping;
import org.apache.commons.dbcp.BasicDataSource;
import org.dom4j.*;
import org.dom4j.io.SAXReader;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;

/**
 * 1.properties配置文件升级为XML配置文件
 * 2.使用面向过程思维去优化代码
 */
public class MyBatisV2 {

    /**
     * 用于存储xml文件中的配置信息 mybatis-config.xml
     */
    private Configuration configuration = new Configuration();

    /**
     * 映射配置文件中配置的命名空间
     */
    private String namespace;

    /**
     * 用来标记 sql 文本中是否有 ${} 以及是否包含动态sql标签
     */
    private boolean isDynamic;

    /**
     * 通过xml文职文件读取配置的内容信息，间隙xml配置文件实现sql的解析
     */
    @Test
    public void test() {
        // 加载全局配置文件 和 映射文件
        LoadXML("mybatis-config.xml");

        Map param = new HashMap();
        param.put("username", "admin");
        param.put("sex", "男");
       // selectList("test.getList", param);
        selectList("test.queryUserByParams", param);
    }

    private <T> List<T> selectList(String statementId, Object param) {
        List<T> results = new ArrayList<T>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            MappedStatement mappedStatement = configuration.getMappedStatementById(statementId);

            // 1.获取连接
            connection = getConnection();
            // 2.获取SQL语句(SqlSource和SqlNode的执行过程) n
            BoundSql boundSql = getSql(mappedStatement, param);
            String sql = boundSql.getSql();
            // 3.创建Statement对象
            statement = createStatement(connection, sql, mappedStatement);

            // 4.设置参数
            setParameters(statement, param, boundSql);

            // 5.执行Statement
            resultSet = handleStatement(statement);

            // 6.处理ResultSet
            handleResultSet(resultSet, results, mappedStatement);
            return results;
        } catch (Exception e) {
            System.out.println(e.getMessage() + e.getStackTrace().toString());
        }

        return null;
    }

    /**
     * 处理结果集
     *
     * @param resultSet
     * @param results
     * @param mappedStatement
     * @param <T>
     */
    private <T> void handleResultSet(ResultSet resultSet, List<T> results, MappedStatement mappedStatement) throws Exception {
        Class clazz = mappedStatement.getResultClass();

        Object result = null;
        while (resultSet.next()) {
            result = clazz.newInstance();
            // 获取表单元数据
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int i = 0; i < columnCount; i++) {
                // 获取数据库表格 ResultSet列明
                String columnName = metaData.getColumnName(i + 1);
                Field field = clazz.getDeclaredField(columnName);
                // 设置访问权限
                field.setAccessible(true);
                field.set(result, resultSet.getObject(i));
            }
            results.add((T) result);
        }
    }

    /**
     * 执行查询
     *
     * @param statement
     * @return
     * @throws SQLException
     */
    private ResultSet handleStatement(Statement statement) throws SQLException {
        if (statement instanceof PreparedStatement) {
            PreparedStatement preparedStatement = (PreparedStatement) statement;
            return preparedStatement.executeQuery();
        }
        return null;
    }

    /**
     * 设置参数
     *
     * @param statement
     * @param param
     * @param boundSql
     */
    private void setParameters(Statement statement, Object param, BoundSql boundSql) throws SQLException {
        if (statement instanceof PreparedStatement) {
            PreparedStatement preparedStatement = (PreparedStatement) statement;
            if (param instanceof Integer || param instanceof String) {
                preparedStatement.setObject(1, param);
            } else if (param instanceof Map) {
                // 结合#{}的处理逻辑进行改造
                Map paramMap = (Map) param;
                List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
                for (int i = 0; i < parameterMappings.size(); i++) {
                    ParameterMapping parameterMapping = parameterMappings.get(i);
                    String name = parameterMapping.getName();
                    // 获取到的参数
                    Object value = paramMap.get(name);
                    Class type = parameterMapping.getType();
                    if (type != null) {
                        //TODO
                        // preparedStatement.setInt(i+1, value);
                        // preparedStatement.setString(i+1, value);
                    } else {
                        preparedStatement.setObject(i + 1, value);
                    }

                }

            } else {
                // TODO
            }
        }
    }

    /**
     * 创建 statement 对象
     *
     * @param connection
     * @param sql
     * @param mappedStatement
     * @return
     */
    private Statement createStatement(Connection connection, String sql, MappedStatement mappedStatement) {
        String statementType = mappedStatement.getStatementType();
        try {
            if (statementType.equals("prepared")) {
                return connection.prepareStatement(sql);
            } else if (statementType.equals("statement")) {
                return connection.createStatement();
            } else if (statementType.equals("callable")) {
                // TODO
            } else {
                // TODO
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 获取 sql 语句
     *
     * @param mappedStatement
     * @param param
     * @return
     */
    private BoundSql getSql(MappedStatement mappedStatement, Object param) {
        SqlSource sqlsource = mappedStatement.getSqlsource();
        BoundSql boundSql = sqlsource.getBoundSql(param);
        return boundSql;
    }

    /**
     * 获取数据库连接
     *
     * @return
     */
    private Connection getConnection() {
        DataSource dataSource = configuration.getDataSource();
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 加载全局配置文件 和 映射文件
     *
     * @param location
     */
    private void LoadXML(String location) {
        // 根据路径获取流对象
        InputStream inputStream = getResourceAsStream(location);
        // dom4j 创建Document对象
        Document document = getDocument(inputStream);

        // 从全局配置文件的根节点<configuration>开始解析
        parseConfiguration(document.getRootElement());
    }

    /**
     * @param rootElement -- <configuration></configuration>
     */
    private void parseConfiguration(Element rootElement) {
        Element environments = rootElement.element("environments");
        // 解析并初始化 DataSource -- BasicDataSource
        parseEnviroments(environments);

        // 解析 mappers 节点
        Element mappers = rootElement.element("mappers");
        parseMappers(mappers);

    }

    /**
     * 解析 mappers 节点
     *
     * @param mappersEle
     */
    private void parseMappers(Element mappersEle) {
        List<Element> list = mappersEle.elements("mapper");
        for (Element element : list) {
            String resource = element.attributeValue("resource");
            InputStream inputStream = getResourceAsStream(resource);
            Document document = getDocument(inputStream);
            parseMapper(document.getRootElement());
        }
    }

    private void parseMapper(Element rootElement) {
        namespace = rootElement.attributeValue("namespace");
        // 获取动态 sql 标签
        List<Element> selectElements = rootElement.elements("select");
        for (Element selectElement : selectElements) {
            parseStatementElement(selectElement);
        }
    }

    /**
     * 解析映射文件中的<select></select>
     *
     * @param selectElement
     */
    private void parseStatementElement(Element selectElement) {
        String statementId = selectElement.attributeValue("id");
        if (statementId == null || statementId.equals("")) {
            return;
        }
        /**
         * 一个CURD标签对应一个MappedStatement对象
         * 一个MappedStatement对象由一个statementId来标识，所以保证唯一性
         * statementId = namespace + "." + CRUD标签的id属性
         */
        statementId = namespace + "." + statementId;

        String resultType = selectElement.attributeValue("resultType");
        Class<?> resultClass = resolveType(resultType);  // resolve 解析

        String statementType = selectElement.attributeValue("statementType");
        statementType = statementType == null || statementType.equals("") ? "prepared" : statementType;

        //TODO SqlSource和SqlNode的封装过程
        SqlSource sqlSource = createSqlSource(selectElement);
        // TODO 建议使用构建者模式去优化
        MappedStatement mappedStatement = new MappedStatement(statementId, resultClass, statementType,
                sqlSource);
        configuration.addMappedStatement(statementId, mappedStatement);
    }

    private SqlSource createSqlSource(Element selectElement) {
        // TODO 其他子标签的解析处理

        SqlSource sqlsource = parseScriptNode(selectElement);
        return sqlsource;
    }

    private SqlSource parseScriptNode(Element selectElement) {
        SqlSource sqlSource;
        // 解析动态标签
        SqlNode mixedSqlNode = parseDynamicTags(selectElement);

        // 如果带有 ${} 或 动态sql标签
        if (isDynamic) {
            sqlSource = new DynamicSqlSource(mixedSqlNode);
        } else {
            sqlSource = new RawSqlSource(mixedSqlNode);
        }
        return sqlSource;
    }

    /**
     * 解析动态标签
     *
     * @param selectElement
     * @return
     */
    private SqlNode parseDynamicTags(Element selectElement) {
        List<SqlNode> sqlNodes = new ArrayList<SqlNode>();

        // 获取 select 标签的子标签-->文本类型、 Element 类型
        int nodeCount = selectElement.nodeCount();
        for (int i = 0; i < nodeCount; i++) {
            Node node = selectElement.node(i);
            if (node instanceof Text) {
                String text = node.getText();
                if (text == null || text.equals("")) {
                    continue;
                }
                TextSqlNode textSqlNode = new TextSqlNode(text.trim());
                if (textSqlNode.isDynamic()) {
                    sqlNodes.add(textSqlNode);
                    isDynamic = true;
                } else {
                    sqlNodes.add(new StaticTextSqlNode(text.trim()));
                }
            } else if (node instanceof Element) {
                isDynamic = true;
                Element element = (Element) node;
                String name = element.getName();
                if (name.equals("if")) {
                    String test = element.attributeValue("test");
                    // 递归解析子元素
                    SqlNode sqlNode = parseDynamicTags(element);

                    IfSqlNode ifSqlNode = new IfSqlNode(test, sqlNode);
                    sqlNodes.add(ifSqlNode);
                } else {
                    // TODO where ...
                }

            } else {
                // TODO ...
            }
        }

        return new MixedSqlNode(sqlNodes);
    }

    /**
     * 根据类全名获取 Class 对象
     *
     * @param resultType
     * @return
     */
    private Class<?> resolveType(String resultType) {
        Class<?> clazz = null;
        try {
            clazz = Class.forName(resultType);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return clazz;
    }

    /**
     * 解析<enviroments></enviroments>标签
     *
     * @param environments
     */
    private void parseEnviroments(Element environments) {
        String defaultValue = environments.attributeValue("default");
        List<Element> environmentList = environments.elements("environment");
        for (Element element : environmentList) {
            String id = element.attributeValue("id");
            if (defaultValue.equals(id)) {
                Element dataSource = element.element("dataSource");
                parseDataSource(dataSource);
            }
        }
    }

    /**
     * 解析数据源<dataSource></dataSource>
     *
     * @param dataSourceEle
     */
    private void parseDataSource(Element dataSourceEle) {
        String type = dataSourceEle.attributeValue("type");
        if (type.equals("DBCP")) {
            BasicDataSource dataSource = new BasicDataSource();
            Properties properties = parseProperties(dataSourceEle);
            dataSource.setDriverClassName(properties.getProperty("driver"));
            dataSource.setUrl(properties.getProperty("url"));
            dataSource.setUsername(properties.getProperty("username"));
            dataSource.setPassword(properties.getProperty("password"));
            this.configuration.setDataSource(dataSource);
        }
    }

    /**
     * 通过<dataSource></dataSource>节点解析<property></property>
     *
     * @param dataSourceEle
     */
    private Properties parseProperties(Element dataSourceEle) {
        Properties properties = new Properties();
        List<Element> elements = dataSourceEle.elements("property");
        for (Element element : elements) {
            String name = element.attributeValue("name");
            String value = element.attributeValue("value");
            properties.put(name, value);
        }
        return properties;
    }

    private Document getDocument(InputStream inputStream) {
        SAXReader saxReader = new SAXReader();
        try {
            return saxReader.read(inputStream);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    private InputStream getResourceAsStream(String location) {
        return this.getClass().getClassLoader().getResourceAsStream(location);
    }
}
