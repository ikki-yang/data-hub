package com.issac.studio.app.source;

import com.issac.studio.app.entity.dto.ExternalParam;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

/**
 * @description: 抽象数据源类
 * @file: Source
 * @author: issac.young
 * @date: 2020/12/1 8:57 上午
 * @since: v1.0.0
 * @copyright (C), 1992-2020, issac
 */
public abstract class Source {

    public void handleSource(SparkSession session, com.issac.studio.app.entity.domain.Source source, ExternalParam eParam) throws Exception {
            Dataset<Row> ds = buildDataset(session, source, eParam);
            registView(source.getTempViewName(), ds);
    }

    /**
     * 根据source的内容读取数据并转换成dataset
     * @param session : session
     * @param source : source
     * @param eParam : eParam
     * @author issac.young
     * @date 2020/12/4 2:29 下午
     * @return org.apache.spark.sql.Dataset<org.apache.spark.sql.Row>
     */
    public abstract Dataset<Row> buildDataset(SparkSession session, com.issac.studio.app.entity.domain.Source source, ExternalParam eParam) throws Exception;

    /**
     * 将某个dataset注册成为一张sparkSQL的临时表
     * @param viewName : viewName
     * @param ds : ds
     * @author issac.young
     * @date 2020/12/4 2:30 下午
     * @return void
     */
    public void registView(String viewName, Dataset<Row> ds){
        ds.createOrReplaceTempView(viewName);
    }
}
