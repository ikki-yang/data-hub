package com.issac.studio.app.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @description: jdbc操作工具类
 * @file: JdbcUtil
 * @author: issac.young
 * @date: 2021/5/11 9:42 上午
 * @since: v1.0.0
 * @copyright (C), 1992-2021, issac
 */
public class JdbcUtil {
    private final static Logger log = LoggerFactory.getLogger(JdbcUtil.class);

    private Connection con;
    private PreparedStatement ps;

    public JdbcUtil(String driverClass, String url, Properties properties) throws Exception {
        try {
            Class.forName(driverClass);
        } catch (ClassNotFoundException e) {
            String msg = String.format("找不到jdbc驱动，error=%s", e);
            throw new Exception(msg);
        }

        try {
            con = DriverManager.getConnection(url, properties);
            log.info("成功建立jdbc链接！");
        } catch (SQLException e) {
            String msg = String.format("建立jdbc链接出错，error=%s", e);
            throw new Exception(msg);
        }
    }

    public Connection getConnection() {
        return con;
    }

    public void exec(String sql) throws Exception {
        try {
            ps = con.prepareStatement(sql);
            log.info("开始执行sql: {}", sql);
            ps.execute(sql);
            log.info("执行sql完成！");
        } catch (Exception e) {
            String msg = String.format("执行sql出错，error=%s\n所执行的sql=%s", e, sql);
            throw new Exception(msg);
        } finally {
            close();
        }
    }

    public void close() throws Exception {
        try {
            if (ps != null && !ps.isClosed()) {
                log.info("JdbcUtil关闭ps!");
                ps.close();
            }
            if (con != null && !con.isClosed()) {
                log.info("JdbcUtil关闭connection!");
                con.close();
            }
        } catch (Exception e) {
            String msg = String.format("JdbcUtil关闭connection或者ps出错，error=%s", e);
            throw new Exception(msg);
        }
    }
}
