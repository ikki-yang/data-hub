package com.issac.studio.app.entity.domain;

import java.util.Date;

/**
 * @description: 记录运行日志
 * @file: TaskLog
 * @author: issac.young
 * @date: 2020/12/4 2:39 下午
 * @since: v1.0.0
 * @copyright (C), 1992-2020, issac
 */
public class TaskLog extends AbstractDomain {
    private String taskKey;
    private Date startTime;
    private Date endTime;
    /**
     * 1：正常日志
     * 2：任务主流程异常日志
     * 3：handle执行异常日志
     * 4：跳过任务执行的日志
     */
    private Integer status;
    private String errorLog;

    public String getTaskKey() {
        return taskKey;
    }

    public void setTaskKey(String taskKey) {
        this.taskKey = taskKey;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getErrorLog() {
        return errorLog;
    }

    public void setErrorLog(String errorLog) {
        this.errorLog = errorLog;
    }
}
