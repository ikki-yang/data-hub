package com.issac.studio.app.sink;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

/**
 * @description: 抽象写数据类
 * @file: Sink
 * @author: issac.young
 * @date: 2020/12/1 2:17 下午
 * @since: v1.0.0
 * @copyright (C), 1992-2020, issac
 */
public abstract class Sink {
    /**
     * 根据sink里面的内容将数据写到相应的目的地
     * @param ds : ds
     * @param sink : sink
     * @author issac.young
     * @date 2020/12/4 2:28 下午
     * @return void
     */
    public abstract void sink(Dataset<Row> ds, com.issac.studio.app.entity.domain.Sink sink) throws Exception;
}
