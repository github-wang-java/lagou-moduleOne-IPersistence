package com.lagou.config;

import com.lagou.io.Resources;
import com.lagou.pojo.Configuration;
import com.lagou.pojo.MappedStatement;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class XMLConfigBuilder {

    private Configuration configuration;

    public XMLConfigBuilder(){
        configuration = new Configuration();
    }

    /**
     * 该方法将配置文件解析并封装至configuration
     * @param inputStream
     * @return
     */
    public Configuration parseConfig(InputStream inputStream) throws DocumentException, PropertyVetoException {
        Document document = new SAXReader().read(inputStream);
        //<configuration>
        Element rootElement = document.getRootElement();
        List<Element> nodes = rootElement.selectNodes("//property");
        Properties properties = new Properties();
        nodes.stream().forEach(e->{
            String name = e.attributeValue("name");
            String value = e.attributeValue("value");
            properties.setProperty(name,value);
        });

        //创建jdbc连接池
        ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();
        comboPooledDataSource.setDriverClass(properties.getProperty("driverClass"));
        comboPooledDataSource.setJdbcUrl(properties.getProperty("jdbcUrl"));
        comboPooledDataSource.setUser(properties.getProperty("username"));
        comboPooledDataSource.setPassword(properties.getProperty("password"));

        configuration.setDataSource(comboPooledDataSource);

        //mapper.xml解析
        List<Element> mapperList = rootElement.selectNodes("//mapper");
        mapperList.stream().forEach(e->{
            String mapperPath = e.attributeValue("resource");
            InputStream mapperInputStream = Resources.getResourceAsSteam(mapperPath);
            XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(configuration);
            try {
                xmlMapperBuilder.parse(mapperInputStream);
            } catch (DocumentException e1) {
                e1.printStackTrace();
            }
        });
        return configuration;
    }

}
