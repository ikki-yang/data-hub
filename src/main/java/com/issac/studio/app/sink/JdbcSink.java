package com.issac.studio.app.sink;


import com.issac.studio.app.entity.domain.Sink;
import com.issac.studio.app.entity.domain.config.sink.JdbcSinkConfig;
import com.issac.studio.app.exception.NullException;
import com.issac.studio.app.exception.TypeException;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * @description: 写数据到Jdbc
 * @file: JdbcSink
 * @author: issac.young
 * @date: 2020/12/5 10:57 上午
 * @since: v1.0.0
 * @copyright (C), 1992-2020, issac
 */
public class JdbcSink extends com.issac.studio.app.sink.Sink {
    private final static Logger log = LoggerFactory.getLogger(JdbcSink.class);

    @Override
    public void sink(Dataset<Row> ds, Sink sink) throws Exception {
        JdbcSinkConfig jdbcSinkConfig;
        if (sink != null) {
            if (sink.getSinkConfigEntity() instanceof JdbcSinkConfig) {
                jdbcSinkConfig = (JdbcSinkConfig) sink.getSinkConfigEntity();
            } else {
                String msg = String.format("写数据描述实体类型异常！预期是{%s}, 实际是{%s}", JdbcSinkConfig.class.getName(), sink.getSinkConfigEntity().getClass().getName());
                throw new TypeException(msg);
            }
        } else {
            String msg = "传入的数据源描述实体为null";
            throw new NullException(msg);
        }
        log.info("开始sink sinkId={}的数据", sink.getId());

        // repartition数量尽量少，这样可以减少集群跟数据库建立的连接数，减少数据库的压力
        Dataset<Row> repartitioned = ds.repartition(jdbcSinkConfig.getPartitionNum());

        Properties prop = new Properties();
        prop.put("driver", jdbcSinkConfig.getDriverClass());
        prop.put("user", jdbcSinkConfig.getUser());
        prop.put("password", jdbcSinkConfig.getPasswd());
        repartitioned.write()
                .mode(jdbcSinkConfig.getSaveMode())
                .jdbc(jdbcSinkConfig.getUrl(), jdbcSinkConfig.getTableName(), prop);

        log.info("sink sinkId={}成功", sink.getId());
    }
}
