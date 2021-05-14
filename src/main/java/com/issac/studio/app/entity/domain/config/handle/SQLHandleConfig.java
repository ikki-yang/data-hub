package com.issac.studio.app.entity.domain.config.handle;

/**
 * @description: sql类型的handle配置信息
 * @file: SQLHandleConfig
 * @author: issac.young
 * @date: 2021/5/11 9:13 上午
 * @since: v1.0.0
 * @copyright (C), 1992-2021, issac
 */
public class SQLHandleConfig extends AbstractHandleConfig {
    private String sql;
    private String url;
    private String driverClass;
    private String user;
    private String passwd;

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDriverClass() {
        return driverClass;
    }

    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }
}
