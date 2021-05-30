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
    private String sourceTable;

    /**
     * hbase表对应的
     */
    private Field[] fields;

    public String getSourceTable() {
        return sourceTable;
    }

    public void setSourceTable(String sourceTable) {
        this.sourceTable = sourceTable;
    }

    public Field[] getFields() {
        return fields;
    }

    public void setFields(Field[] fields) {
        this.fields = fields;
    }
}
