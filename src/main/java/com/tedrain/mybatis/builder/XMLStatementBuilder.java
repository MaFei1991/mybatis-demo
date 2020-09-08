package com.tedrain.mybatis.builder;

import com.tedrain.mybatis.config.Configuration;
import com.tedrain.mybatis.config.MappedStatement;
import com.tedrain.mybatis.sqlsource.iface.SqlSource;
import com.tedrain.mybatis.utils.ReflectUtils;
import org.dom4j.Element;

public class XMLStatementBuilder {
    private Configuration configuration;

    public XMLStatementBuilder(Configuration configuration) {
        this.configuration = configuration;
    }

    public void parseStatementElement(Element selectElement, String namespace) {
        String statementId = selectElement.attributeValue("id");

        if (statementId == null || statementId.equals("")) {
            return;
        }
        // 一个CURD标签对应一个MappedStatement对象
        // 一个MappedStatement对象由一个statementId来标识，所以保证唯一性
        // statementId = namespace + "." + CRUD标签的id属性
        statementId = namespace + "." + statementId;

        // 注意：parameterType参数可以不设置也可以不解析
      /*  String parameterType = selectElement.attributeValue("parameterType");
        Class<?> parameterClass = resolveType(parameterType);*/

        String resultType = selectElement.attributeValue("resultType");
        Class<?> resultClass = ReflectUtils.resolveType(resultType);

        String statementType = selectElement.attributeValue("statementType");
        statementType = statementType == null || statementType == "" ? "prepared" : statementType;

        //TODO SqlSource和SqlNode的封装过程
        SqlSource sqlSource = createSqlSource(selectElement);

        // TODO 建议使用构建者模式去优化
        MappedStatement mappedStatement = new MappedStatement(statementId, resultClass, statementType,
                sqlSource);
        configuration.addMappedStatement(statementId, mappedStatement);
    }
    private SqlSource createSqlSource(Element selectElement) {
        //TODO 其他子标签的解析处理
        XMLScriptBuilder scriptBuilder = new XMLScriptBuilder();
        SqlSource sqlSource = scriptBuilder.parseScriptNode(selectElement);

        return sqlSource;
    }

}
