package com.issac.studio.app.source;

import com.issac.studio.app.entity.domain.config.source.HBaseSourceConfig;
import com.issac.studio.app.entity.dto.ExternalParam;
import com.issac.studio.app.exception.NullException;
import com.issac.studio.app.exception.TypeException;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description: 从HBase读取数据
 * @file: HBaseSource
 * @author: issac.young
 * @date: 2021/5/28 9:42 上午
 * @since: v1.0.0
 * @copyright (C), 1992-2021, issac
 */
public class HBaseSource extends com.issac.studio.app.source.Source{
    private final static Logger log = LoggerFactory.getLogger(HBaseSource.class);

    @Override
    public Dataset<Row> buildDataset(SparkSession session, com.issac.studio.app.entity.domain.Source source, ExternalParam eParam) throws Exception {
        final HBaseSourceConfig hBaseSourceConfig;
        if (source != null) {
            if (source.getSourceConfigEntity() instanceof HBaseSourceConfig) {
                hBaseSourceConfig = (HBaseSourceConfig) source.getSourceConfigEntity();
            } else {
                String msg = String.format("数据源描述实体类型异常！预期是{%s}, 实际是{%s}", HBaseSourceConfig.class.getName(), source.getSourceConfigEntity().getClass().getName());
                throw new TypeException(msg);
            }
        } else {
            String msg = "传入的数据源描述实体为null";
            throw new NullException(msg);
        }
        log.info("开始build sourceId={}数据源的dataset", source.getId());

        return null;
    }
}
