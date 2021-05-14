package com.issac.studio.app.entity.domain;

import com.issac.studio.app.entity.domain.config.source.AbstractSourceConfig;

/**
 * @description: 数据库表实体
 * @file: Source
 * @author: issac.young
 * @date: 2021/5/11 9:21 上午
 * @since: v1.0.0
 * @copyright (C), 1992-2021, issac
 */
public class Source extends AbstractDomain {
    private Long taskId;
    /**
     * 在spark中将读取的数据注册成一张临时表，这个字段就是临时表的名字
     */
    private String tempViewName;
    /**
     * 数据源类的全限定名
     * 例如：com.issac.studio.app.source.JdbcSource
     */
    private String sourceType;
    private String sourceConfigType;
    private String sourceConfigJson;
    private AbstractSourceConfig sourceConfigEntity;

    public Source(Long taskId) {
        this.taskId = taskId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getTempViewName() {
        return tempViewName;
    }

    public void setTempViewName(String tempViewName) {
        this.tempViewName = tempViewName;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getSourceConfigType() {
        return sourceConfigType;
    }

    public void setSourceConfigType(String sourceConfigType) {
        this.sourceConfigType = sourceConfigType;
    }

    public String getSourceConfigJson() {
        return sourceConfigJson;
    }

    public void setSourceConfigJson(String sourceConfigJson) {
        this.sourceConfigJson = sourceConfigJson;
    }

    public AbstractSourceConfig getSourceConfigEntity() {
        return sourceConfigEntity;
    }

    public void setSourceConfigEntity(AbstractSourceConfig sourceConfigEntity) {
        this.sourceConfigEntity = sourceConfigEntity;
    }
}
