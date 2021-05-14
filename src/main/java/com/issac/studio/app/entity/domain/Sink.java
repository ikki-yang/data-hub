package com.issac.studio.app.entity.domain;

import com.issac.studio.app.entity.domain.config.sink.AbstractSinkConfig;

/**
 * @description: 数据库表实体类
 * @file: Sink
 * @author: issac.young
 * @date: 2021/5/11 9:24 上午
 * @since: v1.0.0
 * @copyright (C), 1992-2021, issac
 */
public class Sink extends AbstractDomain {
    private Long taskId;
    private String sinkType;
    private String sinkConfigType;
    private String sinkConfigJson;
    private AbstractSinkConfig sinkConfigEntity;

    public Sink(Long taskId) {
        this.taskId = taskId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getSinkType() {
        return sinkType;
    }

    public void setSinkType(String sinkType) {
        this.sinkType = sinkType;
    }

    public String getSinkConfigType() {
        return sinkConfigType;
    }

    public void setSinkConfigType(String sinkConfigType) {
        this.sinkConfigType = sinkConfigType;
    }

    public String getSinkConfigJson() {
        return sinkConfigJson;
    }

    public void setSinkConfigJson(String sinkConfigJson) {
        this.sinkConfigJson = sinkConfigJson;
    }

    public AbstractSinkConfig getSinkConfigEntity() {
        return sinkConfigEntity;
    }

    public void setSinkConfigEntity(AbstractSinkConfig sinkConfigEntity) {
        this.sinkConfigEntity = sinkConfigEntity;
    }
}
