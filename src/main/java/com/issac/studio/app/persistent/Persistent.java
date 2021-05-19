package com.issac.studio.app.persistent;

import com.issac.studio.app.util.PropertiesUtil;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

/**
 * @description: 解析参数
 * @file: Parser
 * @author: issac.young
 * @date: 2020/12/1 3:07 下午
 * @since: v1.0.0
 * @copyright (C), 1992-2020, issac
 */
public class Persistent {
    private final static Logger log = LoggerFactory.getLogger(Persistent.class);

    private static SqlSession sqlSession;

    static {
        try {
            InputStream resourceAsStream = Resources.getResourceAsStream("mybatis/mybatis.xml");

            Properties prop = new Properties();
            Map<String, String> config = new PropertiesUtil().getPropertyMap();
            prop.put("username", config.get("jdbc.username"));
            prop.put("password", config.get("jdbc.password"));

            SqlSessionFactory build = new SqlSessionFactoryBuilder().build(resourceAsStream, prop);
            sqlSession = build.openSession(true);
            log.info("初始化数据库连接成功！");
        } catch (IOException e) {
            log.error("创建mybatis的SqlSession发生错误，error=", e);
        }
    }

    /**
     * 根据用户传入的类获取对应的持久层操作句柄
     * @param mapperClass : mapperClass
     * @author issac.young
     * @date 2020/12/4 2:32 下午
     * @return T
     */
    public static <T> T getMapper(Class<T> mapperClass) {
        return sqlSession.getMapper(mapperClass);
    }
}
