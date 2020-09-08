package com.tedrain.mybatis.builder;

import com.tedrain.mybatis.config.Configuration;
import com.tedrain.mybatis.io.Resources;
import com.tedrain.mybatis.utils.DocumentUtils;
import org.apache.commons.dbcp.BasicDataSource;
import org.dom4j.Document;
import org.dom4j.Element;

import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public class XMLConfigBuilder {
    private Configuration configuration;

    public XMLConfigBuilder() {
        this.configuration = configuration;
    }

    /**
     *
     * @param rootElement <configuration></configuration>
     */
    public Configuration parseConfiguration(Element rootElement) {
        Element environments = rootElement.element("environments");
        parseEnvironments(environments);

        Element mappers = rootElement.element("mappers");
        parseMappers(mappers);
        return configuration;
    }

    /**
     * 解析全局配置文件中的mappers标签
     * @param mappers <mappers></mappers>
     */
    private void parseMappers(Element mappers) {
        List<Element> list = mappers.elements("mapper");
        for (Element element : list) {
            String resource = element.attributeValue("resource");
            // 根据xml的路径，获取对应的输入流
            InputStream inputStream = Resources.getResourceAsStream(resource);
            // 将流对象，转换成Document对象
            Document document = DocumentUtils.getDocument(inputStream);
            // 针对Document对象，按照Mybatis的语义去解析Document
            XMLMapperBuilder mapperBuilder = new XMLMapperBuilder(configuration);
            mapperBuilder.parseMapper(document.getRootElement());
        }
    }



    /**
     * 解析全局配置文件中的environments标签
     * @param environments <environments></environments>
     */
    private void parseEnvironments(Element environments) {
        String defaultValue = environments.attributeValue("default");
        List<Element> environmentList = environments.elements("environment");
        for (Element element : environmentList) {
            String id = element.attributeValue("id");
            if (defaultValue.equals(id)){
                Element dataSource = element.element("dataSource");
                parseDataSource(dataSource);
            }
        }
    }

    /**
     * 解析数据源标签
     * @param element <dataSource></dataSource>
     */
    private void parseDataSource(Element element) {
        String type = element.attributeValue("type");
        if ("DBCP".equals(type)){
            BasicDataSource dataSource = new BasicDataSource();

            Properties properties = parseProperties(element);

            dataSource.setDriverClassName(properties.getProperty("driver"));
            dataSource.setUrl(properties.getProperty("url"));
            dataSource.setUsername(properties.getProperty("username"));
            dataSource.setPassword(properties.getProperty("password"));

            configuration.setDataSource(dataSource);
        }

    }

    /**
     *
     * @param element dataSource标签
     * @return
     */
    private Properties parseProperties(Element element) {
        Properties properties = new Properties();
        List<Element> propertyList = element.elements("property");
        for (Element propertyElement : propertyList) {
            String name = propertyElement.attributeValue("name");
            String value = propertyElement.attributeValue("value");
            properties.put(name,value);
        }
        return properties;
    }

}
