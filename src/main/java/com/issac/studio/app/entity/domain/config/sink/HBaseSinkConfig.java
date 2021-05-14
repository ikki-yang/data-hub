package com.issac.studio.app.entity.domain.config.sink;

/**
 * @description: 写HBASE的配置信息
 * @file: HBaseSinkConfig
 * @author: issac.young
 * @date: 2021/5/11 9:23 上午
 * @since: v1.0.0
 * @copyright (C), 1992-2021, issac
 */
public class HBaseSinkConfig extends AbstractSinkConfig {
    private String targetTable;
    private String hFilePath;
    /**
     * 是否清空表
     * 1：清空
     * 其他：不清空
     */
    private Integer isTruncate;
    /**
     * 清空表的同时是否保留原有分区信息
     *  1：保留
     *  其他：不保留
     */
    private Integer isPreserve;
    /**
     * 格式为"hbase.mapreduce.bulkload.max.hfiles.perRegion.perFamily:3200,xxx.xxx.xx:323"
     */
    private String hBaseConfig;

    public String getTargetTable() {
        return targetTable;
    }

    public void setTargetTable(String targetTable) {
        this.targetTable = targetTable;
    }

    public String gethFilePath() {
        return hFilePath;
    }

    public void sethFilePath(String hFilePath) {
        this.hFilePath = hFilePath;
    }

    public Integer getIsTruncate() {
        return isTruncate;
    }

    public void setIsTruncate(Integer isTruncate) {
        this.isTruncate = isTruncate;
    }

    public Integer getIsPreserve() {
        return isPreserve;
    }

    public void setIsPreserve(Integer isPreserve) {
        this.isPreserve = isPreserve;
    }

    public String gethBaseConfig() {
        return hBaseConfig;
    }

    public void sethBaseConfig(String hBaseConfig) {
        this.hBaseConfig = hBaseConfig;
    }
}
