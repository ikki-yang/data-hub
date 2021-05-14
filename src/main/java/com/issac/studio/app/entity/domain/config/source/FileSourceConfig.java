package com.issac.studio.app.entity.domain.config.source;

/**
 * @description: 类csv型文件读取配置类
 * @file: FileSourceConfig
 * @author: issac.young
 * @date: 2021/5/11 9:20 上午
 * @since: v1.0.0
 * @copyright (C), 1992-2021, issac
 */
public class FileSourceConfig extends AbstractSourceConfig {
    private String location;
    private Field[] fields;
    private String separator;
    private String rowSeparator;
    private String charsetName;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Field[] getFields() {
        return fields;
    }

    public void setFields(Field[] fields) {
        this.fields = fields;
    }

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public String getRowSeparator() {
        return rowSeparator;
    }

    public void setRowSeparator(String rowSeparator) {
        this.rowSeparator = rowSeparator;
    }

    public String getCharsetName() {
        return charsetName;
    }

    public void setCharsetName(String charsetName) {
        this.charsetName = charsetName;
    }
}
