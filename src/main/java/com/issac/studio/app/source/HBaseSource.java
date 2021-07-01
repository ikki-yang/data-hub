package com.issac.studio.app.source;

import com.issac.studio.app.entity.domain.config.source.Description;
import com.issac.studio.app.entity.domain.config.source.Field;
import com.issac.studio.app.entity.domain.config.source.HBaseSourceConfig;
import com.issac.studio.app.entity.dto.ExternalParam;
import com.issac.studio.app.exception.NullException;
import com.issac.studio.app.exception.TypeException;
import com.issac.studio.app.util.TypeUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableInputFormat;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.spark.api.java.JavaRDD;
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

/**
 * @description: 从HBase读取数据
 * @file: HBaseSource
 * @author: issac.young
 * @date: 2021/5/28 9:42 上午
 * @since: v1.0.0
 * @copyright (C), 1992-2021, issac
 */
public class HBaseSource extends com.issac.studio.app.source.Source {
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

        String tableName = hBaseSourceConfig.getTableName();
        Description description = hBaseSourceConfig.getDescription();
        String columnFamily = description.getColumnFamily();
        String[] qualifiers = description.getQualifiers();
        Field[] fields = description.getFields();

        ArrayList<StructField> structFields = new ArrayList<>();
        for (Field field : fields) {
            StructField structField = DataTypes.createStructField(
                    field.getName(), TypeUtil.typeMap(field.getType()), true);
            structFields.add(structField);
        }
        StructType schema = DataTypes.createStructType(structFields);

        Configuration conf = HBaseConfiguration.create();
        conf.set(TableInputFormat.INPUT_TABLE, tableName);

        JavaRDD<Tuple2<ImmutableBytesWritable, Result>> hbaseRDD = session.sparkContext().newAPIHadoopRDD(conf, TableInputFormat.class,
                ImmutableBytesWritable.class, Result.class).toJavaRDD();

        JavaRDD<Row> rowJavaRDD = hbaseRDD.map(new Function<Tuple2<ImmutableBytesWritable, Result>, Row>() {
            @Override
            public Row call(Tuple2<ImmutableBytesWritable, Result> v1) throws Exception {
                Result result = v1._2;
                String[] values = new String[qualifiers.length];
                for (int i = 0; i < qualifiers.length; i++) {
                    String qualifier = qualifiers[i];
                    Cell cell = result.getColumnLatestCell(Bytes.toBytes(columnFamily), Bytes.toBytes(qualifier));
                    if (cell != null) {
                        values[i] = Bytes.toString(cell.getValueArray());
                    } else {
                        values[i] = null;
                    }
                }
                Object[] cols = TypeUtil.typeFormat(values, schema);
                return RowFactory.create(cols);
            }
        });

        log.info("build sourceId={}数据源成功", source.getId());
        return session.createDataFrame(rowJavaRDD, schema);
    }
}
