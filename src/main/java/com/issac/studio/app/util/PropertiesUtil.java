package com.issac.studio.app.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @description: properties文件读取工具类
 * @file: PropertiesUtil
 * @author: issac.young
 * @date: 2021/5/11 9:42 上午
 * @since: v1.0.0
 * @copyright (C), 1992-2021, issac
 */
public class PropertiesUtil {
    private final static Logger log = LoggerFactory.getLogger(PropertiesUtil.class);

    private Map<String, String> propertyMap = new HashMap<>();

    public PropertiesUtil() {
        log.info("开始加载配置文件/env/app.properties");
        InputStream in = this.getClass().getResourceAsStream("/env/app.properties");
        Properties properties = new Properties();
        try {
            properties.load(in);
            Enumeration<?> enumeration = properties.propertyNames();
            while (enumeration.hasMoreElements()) {
                String element = enumeration.nextElement().toString();
                propertyMap.put(element, properties.getProperty(element));
            }
            log.info("加载配置文件到内存成功！");
        } catch (Exception e) {
            log.error("读取配置文件失败！error=", e);
        }
    }

    public Map<String, String> getPropertyMap() {
        return propertyMap;
    }

    public void setPropertyMap(Map<String, String> propertyMap) {
        this.propertyMap = propertyMap;
    }
}
