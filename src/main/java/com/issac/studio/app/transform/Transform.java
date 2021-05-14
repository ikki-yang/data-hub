package com.issac.studio.app.transform;

import com.issac.studio.app.entity.dto.ExternalParam;
import com.issac.studio.app.param.ParamHandler;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

/**
 * @description: 根据SQL做etl的类
 * @file: Transform
 * @author: issac.young
 * @date: 2020/12/1 9:10 上午
 * @since: v1.0.0
 * @copyright (C), 1992-2020, issac
 */
public class Transform {
    /**
     * 执行sparkSQL
     * @param session : session
     * @param sql : sql
     * @param eParam : eParam
     * @author issac.young
     * @date 2020/12/4 2:31 下午
     * @return org.apache.spark.sql.Dataset<org.apache.spark.sql.Row>
     */
    public static Dataset<Row> execute(SparkSession session, String sql, ExternalParam eParam) throws Exception {
        String handledSQL = ParamHandler.handleSQL(sql, eParam.getParamDt());
        return session.sql(handledSQL);
    }
}
