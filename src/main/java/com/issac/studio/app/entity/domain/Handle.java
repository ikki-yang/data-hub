package com.issac.studio.app.entity.domain;

import com.issac.studio.app.entity.domain.config.handle.AbstractHandleConfig;

/**
 * @description: 数据库表实体
 * @file: Handle
 * @author: issac.young
 * @date: 2021/5/11 9:12 上午
 * @since: v1.0.0
 * @copyright (C), 1992-2021, issac
 */
public class Handle extends AbstractDomain {
    private Long taskId;

    /**
     * handle执行类型
     * 1：前置
     * 2：后置
     * 3：异常
     */
    private Integer executeType;
    /**
     * 具体的handle的类全路径名
     * 例如：com.issac.studio.app.handle.SQLHandle
     */
    private String handleType;
    /**
     * 具体的HandleConfig的类全路径名
     * 例如：com.issac.studio.app.entity.handle.SqlHandleConfig
     */
    private String handleConfigType;
    /**
     * 与handleConfigType对应的配置类的json
     */
    private String handleConfigJson;
    /**
     * handleConfigJson这个json对应的具体类实体
     */
    private AbstractHandleConfig handleConfigEntity;

    public Handle() {
    }

    public Handle(Long taskId, Integer executeType) {
        this.taskId = taskId;
        this.executeType = executeType;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Integer getExecuteType() {
        return executeType;
    }

    public void setExecuteType(Integer executeType) {
        this.executeType = executeType;
    }

    public String getHandleType() {
        return handleType;
    }

    public void setHandleType(String handleType) {
        this.handleType = handleType;
    }

    public String getHandleConfigType() {
        return handleConfigType;
    }

    public void setHandleConfigType(String handleConfigType) {
        this.handleConfigType = handleConfigType;
    }

    public String getHandleConfigJson() {
        return handleConfigJson;
    }

    public void setHandleConfigJson(String handleConfigJson) {
        this.handleConfigJson = handleConfigJson;
    }

    public AbstractHandleConfig getHandleConfigEntity() {
        return handleConfigEntity;
    }

    public void setHandleConfigEntity(AbstractHandleConfig handleConfigEntity) {
        this.handleConfigEntity = handleConfigEntity;
    }
}
