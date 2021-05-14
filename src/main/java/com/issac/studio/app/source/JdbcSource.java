package com.issac.studio.app.source;

import com.issac.studio.app.entity.domain.Source;
import com.issac.studio.app.entity.dto.ExternalParam;
import com.issac.studio.app.entity.domain.config.source.JdbcSourceConfig;
import com.issac.studio.app.exception.NullException;
import com.issac.studio.app.exception.TypeException;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description: 从jdbc读取数据
 * @file: OracleSource
 * @author: issac.young
 * @date: 2020/12/5 6:58 下午
 * @since: v1.0.0
 * @copyright (C), 1992-2020, issac
 */
public class JdbcSource extends com.issac.studio.app.source.Source {
    private final static Logger log = LoggerFactory.getLogger(JdbcSource.class);

    @Override
    public Dataset<Row> buildDataset(SparkSession session, Source source, ExternalParam eParam)  throws Exception {
        final JdbcSourceConfig jdbcSourceConfig;
        if (source != null) {
            if (source.getSourceConfigEntity() instanceof JdbcSourceConfig) {
                jdbcSourceConfig = (JdbcSourceConfig) source.getSourceConfigEntity();
            } else {
                String msg = String.format("数据源描述实体类型异常！预期是{%s}, 实际是{%s}", JdbcSourceConfig.class.getName(), source.getSourceConfigEntity().getClass().getName());
                throw new TypeException(msg);
            }
        } else {
            String msg = "传入的数据源描述实体为null";
            throw new NullException(msg);
        }
        log.info("开始build sourceId={}数据源的dataset", source.getId());

        Dataset<Row> ds = session.read().format("jdbc")
                .option("url", jdbcSourceConfig.getUrl())
                .option("driver", jdbcSourceConfig.getDriverClass())
                .option("dbtable", jdbcSourceConfig.getTableName())
                .option("user", jdbcSourceConfig.getUser())
                .option("password", jdbcSourceConfig.getPasswd())
                .option("partitionColumn", jdbcSourceConfig.getPartitionColumn())
                .option("lowerBound", jdbcSourceConfig.getLowerBound())
                .option("upperBound", jdbcSourceConfig.getUpperBound())
                .option("numPartitions", jdbcSourceConfig.getNumPartitions())
                .load();

        log.info("build sourceId={}数据源成功", source.getId());
        return ds;
    }
}
