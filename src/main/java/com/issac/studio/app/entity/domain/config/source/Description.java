package com.issac.studio.app.entity.domain.config.source;

import java.io.Serializable;

/**
 * @description: hbase表结构信息
 * @file: Description
 * @author: issac.young
 * @date: 2021/5/11 9:19 上午
 * @since: v1.0.0
 * @copyright (C), 1992-2021, issac
 */
public class Description implements Serializable {
    private String columnFamily;
    private String[] qualifiers;
    private Field[] fields;

    public String getColumnFamily() {
        return columnFamily;
    }

    public void setColumnFamily(String columnFamily) {
        this.columnFamily = columnFamily;
    }

    public String[] getQualifiers() {
        return qualifiers;
    }

    public void setQualifiers(String[] qualifiers) {
        this.qualifiers = qualifiers;
    }

    public Field[] getFields() {
        return fields;
    }

    public void setFields(Field[] fields) {
        this.fields = fields;
    }
}
