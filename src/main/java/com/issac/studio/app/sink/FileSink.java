package com.issac.studio.app.sink;

import com.issac.studio.app.entity.domain.config.sink.FileSinkConfig;
import com.issac.studio.app.entity.domain.config.sink.JdbcSinkConfig;
import com.issac.studio.app.entity.dto.ExternalParam;
import com.issac.studio.app.exception.NullException;
import com.issac.studio.app.exception.TypeException;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileSink extends Sink{
    private final static Logger log = LoggerFactory.getLogger(JdbcSink.class);

    @Override
    public void sink(Dataset<Row> ds, com.issac.studio.app.entity.domain.Sink sink, ExternalParam eParam) throws Exception {
        FileSinkConfig fileSinkConfig;
        if (sink != null) {
            if (sink.getSinkConfigEntity() instanceof FileSinkConfig) {
                fileSinkConfig = (FileSinkConfig) sink.getSinkConfigEntity();
            } else {
                String msg = String.format("写数据描述实体类型异常！预期是{%s}, 实际是{%s}", FileSinkConfig.class.getName(), sink.getSinkConfigEntity().getClass().getName());
                throw new TypeException(msg);
            }
        } else {
            String msg = "传入的数据源描述实体为null";
            throw new NullException(msg);
        }
        log.info("开始sink sinkId={}的数据", sink.getId());

        String filePath = fileSinkConfig.getFilePath();
        String fileName = fileSinkConfig.getFileName();
        String fileFormat = fileSinkConfig.getFileFormat();
        Integer partitionNum = fileSinkConfig.getPartitionNum();

        if(null != partitionNum){
            ds.repartition(partitionNum).write().format(fileFormat).save(filePath + "/" + fileName);
        }else {
            ds.write().format(fileFormat).save(filePath + "/" + fileName);
        }

        log.info("sink sinkId={}成功", sink.getId());
    }
}
