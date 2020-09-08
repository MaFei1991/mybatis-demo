package com.tedrain.mybatis.builder;

import com.tedrain.mybatis.config.Configuration;
import org.dom4j.Element;

import java.util.List;

public class XMLMapperBuilder {
    private Configuration configuration;

    public XMLMapperBuilder(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * 解析映射文件的mapper信息
     * @param rootElement <mapper></mapper>
     */
    public void parseMapper(Element rootElement) {
        String namespace = rootElement.attributeValue("namespace");
        // TODO 获取动态SQL标签，比如<sql>
        // TODO 获取其他标签
        List<Element> selectElements = rootElement.elements("select");
        for (Element selectElement : selectElements) {
            XMLStatementBuilder statementBuilder = new XMLStatementBuilder(configuration);
            statementBuilder.parseStatementElement(selectElement,namespace);
        }
    }

}
