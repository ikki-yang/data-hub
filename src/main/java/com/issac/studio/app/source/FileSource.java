package com.issac.studio.app.source;


import com.issac.studio.app.entity.domain.Source;
import com.issac.studio.app.entity.domain.config.source.Field;
import com.issac.studio.app.entity.domain.config.source.FileSourceConfig;
import com.issac.studio.app.entity.dto.ExternalParam;
import com.issac.studio.app.exception.NullException;
import com.issac.studio.app.exception.TypeException;
import com.issac.studio.app.param.ParamHandler;
import com.issac.studio.app.util.TypeUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @description: hdfs文件类型的数据源处理类
 * @file: HdfsSource
 * @author: issac.young
 * @date: 2020/12/1 9:09 上午
 * @since: v1.0.0
 * @copyright (C), 1992-2020, issac
 */
public class FileSource extends com.issac.studio.app.source.Source {
    private final static Logger log = LoggerFactory.getLogger(FileSource.class);

    @Override
    public Dataset<Row> buildDataset(SparkSession session, Source source, ExternalParam eParam) throws Exception {
        final FileSourceConfig fileSourceConfig;
        if (source != null) {
            if (source.getSourceConfigEntity() instanceof FileSourceConfig) {
                fileSourceConfig = (FileSourceConfig) source.getSourceConfigEntity();
            } else {
                String msg = String.format("数据源描述实体类型异常！预期是{%s}, 实际是{%s}", FileSourceConfig.class.getName(), source.getSourceConfigEntity().getClass().getName());
                throw new TypeException(msg);
            }
        } else {
            String msg = "传入的数据源描述实体为null";
            throw new NullException(msg);
        }
        log.info("开始build sourceId={}数据源的dataset", source.getId());

        ArrayList<StructField> structFields = new ArrayList<>();
        for (Field field : fileSourceConfig.getFields()) {
            StructField structField = DataTypes.createStructField(
                    field.getName(), TypeUtil.typeMap(field.getType()), true);
            structFields.add(structField);
        }
        StructType schema = DataTypes.createStructType(structFields);
        String handledLoc = ParamHandler.handlePath(fileSourceConfig.getLocation(), eParam.getParamDt());

        JavaRDD<String> stringRDD;
        if (StringUtils.isNotBlank(fileSourceConfig.getRowSeparator())) {
            // 当存在特殊换行符的时候，采用一次性读取所有文件内容再切分的方式
            JavaRDD<Tuple2<String, String>> javaRDD =
                    session.sparkContext().wholeTextFiles(handledLoc, 3).toJavaRDD();
            stringRDD = javaRDD.flatMap(new FlatMapFunction<Tuple2<String, String>, String>() {
                @Override
                public Iterator<String> call(Tuple2<String, String> tuple2) throws Exception {
                    String wholeStr = new String(tuple2._2.getBytes(), fileSourceConfig.getCharsetName());
                    String[] rowStrs = wholeStr.split(fileSourceConfig.getRowSeparator());

                    ArrayList<String> list = new ArrayList<>();
                    for (String item : rowStrs) {
                        if (!"".equals(item)) {
                            list.add(item);
                        }
                    }
                    return list.iterator();
                }
            });
        } else {
            stringRDD = session.read().textFile(handledLoc).javaRDD();
        }

        JavaRDD<Row> rowJavaRDD = stringRDD.map(new Function<String, Row>() {
            @Override
            public Row call(String s) throws Exception {
                String[] split = s.split(fileSourceConfig.getSeparator(), -1);
                Object[] cols = TypeUtil.typeFormat(split, schema);
                return RowFactory.create(cols);
            }
        });
        log.info("build sourceId={}数据源成功", source.getId());
        return session.createDataFrame(rowJavaRDD, schema);
    }
}
