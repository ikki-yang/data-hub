package com.issac.studio.app.entity.domain;

/**
 * @description: 需要执行的任务的编号
 * @file: Task
 * @author: issac.young
 * @date: 2020/12/1 5:03 下午
 * @since: v1.0.0
 * @copyright (C), 1992-2020, issac
 */
public class Task extends AbstractDomain {
    private String taskKey;
    private String taskName;
    private String jarPath;
    private String transformSql;
    /**
     * 是否需要读取或者写hive表 1：需要；2：不需要
     */
    private Integer hiveSupport;

    /**
     * spark提交集群的配置（以json格式存储）
     */
    private String sparkConfig;

    /**
     * 是否执行该任务的开关
     * 1：执行
     * 2：跳过
     */
    private Integer latch;

    public Task() {
    }

    public Task(String taskKey) {
        this.taskKey = taskKey;
    }

    public String getTaskKey() {
        return taskKey;
    }

    public void setTaskKey(String taskKey) {
        this.taskKey = taskKey;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getJarPath() {
        return jarPath;
    }

    public void setJarPath(String jarPath) {
        this.jarPath = jarPath;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTransformSql() {
        return transformSql;
    }

    public void setTransformSql(String transformSql) {
        this.transformSql = transformSql;
    }

    public Integer getHiveSupport() {
        return hiveSupport;
    }

    public void setHiveSupport(Integer hiveSupport) {
        this.hiveSupport = hiveSupport;
    }

    public String getSparkConfig() {
        return sparkConfig;
    }

    public void setSparkConfig(String sparkConfig) {
        this.sparkConfig = sparkConfig;
    }

    public Integer getLatch() {
        return latch;
    }

    public void setLatch(Integer latch) {
        this.latch = latch;
    }
}
