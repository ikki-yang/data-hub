package com.issac.studio.app.sink;


import com.issac.studio.app.entity.domain.Sink;
import com.issac.studio.app.entity.domain.config.sink.HiveSinkConfig;
import com.issac.studio.app.entity.dto.ExternalParam;
import com.issac.studio.app.exception.NullException;
import com.issac.studio.app.exception.TypeException;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description: 写数据到hive的类
 * @file: HiveSink
 * @author: issac.young
 * @date: 2020/12/1 2:19 下午
 * @since: v1.0.0
 * @copyright (C), 1992-2020, issac
 */
public class HiveSink extends com.issac.studio.app.sink.Sink {
    private final static Logger log = LoggerFactory.getLogger(HiveSink.class);

    @Override
    public void sink(Dataset<Row> ds, Sink sink, ExternalParam eParam) throws Exception {
        HiveSinkConfig hiveSinkConfig;
        if (sink != null) {
            if (sink.getSinkConfigEntity() instanceof HiveSinkConfig) {
                hiveSinkConfig = (HiveSinkConfig) sink.getSinkConfigEntity();
            } else {
                String msg = String.format("写数据描述实体类型异常！预期是{%s}, 实际是{%s}", HiveSinkConfig.class.getName(), sink.getSinkConfigEntity().getClass().getName());
                throw new TypeException(msg);
            }
        } else {
            String msg = "传入的数据源描述实体为null";
            throw new NullException(msg);
        }
        log.info("开始sink sinkId={}的数据", sink.getId());

        Dataset<Row> resultDs = ds.repartition(hiveSinkConfig.getPartitionNum());
        resultDs.write().mode(hiveSinkConfig.getInsertMode()).insertInto(hiveSinkConfig.getTargetTable());
        log.info("sink sinkId={}成功", sink.getId());
    }
}
