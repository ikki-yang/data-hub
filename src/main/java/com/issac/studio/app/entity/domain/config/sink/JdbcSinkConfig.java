package com.issac.studio.app.entity.domain.config.sink;

/**
 * @description: 写jdbc的配置信息
 * @file: JdbcSinkConfig
 * @author: issac.young
 * @date: 2021/5/11 9:24 上午
 * @since: v1.0.0
 * @copyright (C), 1992-2021, issac
 */
public class JdbcSinkConfig extends AbstractSinkConfig {
    private String url;
    private String driverClass;
    private String tableName;
    private String user;
    private String passwd;
    private Integer partitionNum;
    private String saveMode;

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

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
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

    public Integer getPartitionNum() {
        return partitionNum;
    }

    public void setPartitionNum(Integer partitionNum) {
        this.partitionNum = partitionNum;
    }

    public String getSaveMode() {
        return saveMode;
    }

    public void setSaveMode(String saveMode) {
        this.saveMode = saveMode;
    }
}
