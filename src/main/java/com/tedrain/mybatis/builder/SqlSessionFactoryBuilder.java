package com.tedrain.mybatis.builder;

import com.tedrain.mybatis.config.Configuration;
import com.tedrain.mybatis.factory.DefaultSqlSessionFactory;
import com.tedrain.mybatis.factory.SqlSessionFactory;
import com.tedrain.mybatis.utils.DocumentUtils;
import org.dom4j.Document;

import java.io.InputStream;
import java.io.Reader;

public class SqlSessionFactoryBuilder {
    public SqlSessionFactory build(InputStream inputStream){
        Document document = DocumentUtils.getDocument(inputStream);

        // 根据InputStream流获取Configuration对象
        XMLConfigBuilder configBuilder = new XMLConfigBuilder();
        Configuration configuration = configBuilder.parseConfiguration(document.getRootElement());
        return build(configuration);
    }

    public SqlSessionFactory build(Reader reader){
        // 根据Reader流获取Configuration对象
        return null;
    }

    private SqlSessionFactory build(Configuration configuration){
        return new DefaultSqlSessionFactory(configuration);
    }
}
