package com.issac.studio.app.entity.domain.config.sink;

/**
 * @description: 写Hive的配置信息
 * @file: HiveSinkConfig
 * @author: issac.young
 * @date: 2021/5/11 9:24 上午
 * @since: v1.0.0
 * @copyright (C), 1992-2021, issac
 */
public class HiveSinkConfig extends AbstractSinkConfig {
    /**
     * 写hive的模式
     * overwrite：覆写
     * append：追加
     * ignore：在将dataFrame保存的过程中，如果发现数据已经有了，则保留原有的数据，新数据不保存
     */
    private String insertMode;
    private String targetTable;
    private Integer partitionNum;

    public String getInsertMode() {
        return insertMode;
    }

    public void setInsertMode(String insertMode) {
        this.insertMode = insertMode;
    }

    public String getTargetTable() {
        return targetTable;
    }

    public void setTargetTable(String targetTable) {
        this.targetTable = targetTable;
    }

    public Integer getPartitionNum() {
        return partitionNum;
    }

    public void setPartitionNum(Integer partitionNum) {
        this.partitionNum = partitionNum;
    }
}
