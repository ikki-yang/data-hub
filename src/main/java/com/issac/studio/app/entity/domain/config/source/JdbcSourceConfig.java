package com.issac.studio.app.entity.domain.config.source;

/**
 * @description: 读取jdbc数据的配置类
 * @file: JdbcSourceConfig
 * @author: issac.young
 * @date: 2021/5/11 9:20 上午
 * @since: v1.0.0
 * @copyright (C), 1992-2021, issac
 */
public class JdbcSourceConfig extends AbstractSourceConfig {
    private String url;
    private String driverClass;
    private String tableName;
    private String user;
    private String passwd;
    private String partitionColumn;
    private Integer numPartitions;
    private Long lowerBound;
    private Long upperBound;

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

    public String getPartitionColumn() {
        return partitionColumn;
    }

    public void setPartitionColumn(String partitionColumn) {
        this.partitionColumn = partitionColumn;
    }

    public Integer getNumPartitions() {
        return numPartitions;
    }

    public void setNumPartitions(Integer numPartitions) {
        this.numPartitions = numPartitions;
    }

    public Long getLowerBound() {
        return lowerBound;
    }

    public void setLowerBound(Long lowerBound) {
        this.lowerBound = lowerBound;
    }

    public Long getUpperBound() {
        return upperBound;
    }

    public void setUpperBound(Long upperBound) {
        this.upperBound = upperBound;
    }
}
