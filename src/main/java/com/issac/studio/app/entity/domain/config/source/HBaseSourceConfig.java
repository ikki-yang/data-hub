package com.issac.studio.app.entity.domain.config.source;

/**
 * @description: 读取hbase数据的配置类
 * @file: HBaseSourceConfig
 * @author: issac.young
 * @date: 2021/5/11 9:20 上午
 * @since: v1.0.0
 * @copyright (C), 1992-2021, issac
 */
public class HBaseSourceConfig extends AbstractSourceConfig {
    /**
     * 要读取的hbase表名
     */
    private String tableName;

    private Description description;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Description getDescription() {
        return description;
    }

    public void setDescription(Description description) {
        this.description = description;
    }
}
