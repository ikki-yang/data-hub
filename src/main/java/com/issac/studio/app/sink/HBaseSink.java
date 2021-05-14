package com.issac.studio.app.sink;

import com.issac.studio.app.entity.domain.Sink;
import com.issac.studio.app.entity.domain.config.sink.HBaseSinkConfig;
import com.issac.studio.app.exception.NullException;
import com.issac.studio.app.exception.TypeException;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HRegionInfo;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapred.TableOutputFormat;
import org.apache.hadoop.hbase.mapreduce.HFileOutputFormat2;
import org.apache.hadoop.hbase.mapreduce.LoadIncrementalHFiles;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.mapreduce.Job;
import org.apache.spark.Partitioner;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.function.PairFlatMapFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Tuple2;

import java.util.*;

/**
 * @description: 写HBASE
 * @file: HBaseSink
 * @author: issac.young
 * @date: 2021/5/11 9:29 上午
 * @since: v1.0.0
 * @copyright (C), 1992-2021, issac
 */
public class HBaseSink extends com.issac.studio.app.sink.Sink {
    private final static Logger log = LoggerFactory.getLogger(HBaseSink.class);

    @Override
    public void sink(Dataset<Row> ds, Sink sink) throws Exception {
        HBaseSinkConfig hbaseSinkConfig;
        if (sink != null) {
            if (sink.getSinkConfigEntity() instanceof HBaseSinkConfig) {
                hbaseSinkConfig = (HBaseSinkConfig) sink.getSinkConfigEntity();
            } else {
                String msg = String.format("写数据描述实体类型异常！预期是{%s}, 实际是{%s}", HBaseSinkConfig.class.getName(), sink.getSinkConfigEntity().getClass().getName());
                throw new TypeException(msg);
            }
        } else {
            String msg = "传入的数据源描述实体为null";
            throw new NullException(msg);
        }
        log.info("开始sink sinkId={}的数据", sink.getId());

        String tableName = hbaseSinkConfig.getTargetTable();
        String hFilePath = hbaseSinkConfig.gethFilePath();
        Integer isTruncate = hbaseSinkConfig.getIsTruncate();
        Integer isPreserve = hbaseSinkConfig.getIsPreserve();
        String hbaseConfig = hbaseSinkConfig.gethBaseConfig();

        JavaPairRDD<ImmutableBytesWritable, KeyValue> hFileRdd = ds.javaRDD()
                .flatMapToPair(new PairFlatMapFunction<Row, ImmutableBytesWritable, KeyValue>() {
                    @Override
                    public Iterator<Tuple2<ImmutableBytesWritable, KeyValue>> call(Row row) throws Exception {
                        String rowKey = row.getString(0); // 按照约定，第一个字段是rowKey

                        ArrayList<Tuple2<ImmutableBytesWritable, KeyValue>> list = new ArrayList<>();
                        for (int i = 1; i < row.length(); i++) {
                            String fieldName = row.schema().fields()[i].name();
                            String columnFamily = fieldName.split(":")[0];
                            String qualifier = fieldName.split(":")[1];
                            String value = String.valueOf(row.get(i));
                            KeyValue keyValue = new KeyValue(
                                    Bytes.toBytes(rowKey),
                                    Bytes.toBytes(columnFamily),
                                    Bytes.toBytes(qualifier),
                                    Bytes.toBytes(value));
                            list.add(new Tuple2<>(new ImmutableBytesWritable(Bytes.toBytes(rowKey)), keyValue));
                        }

                        return list.iterator();
                    }
                });

        Configuration conf = HBaseConfiguration.create();
        conf.set(TableOutputFormat.OUTPUT_TABLE, tableName);
        HashMap<String, String> hBaseConfigMap = parseHBaseConfig(hbaseConfig);
        for (Map.Entry<String, String> entry : hBaseConfigMap.entrySet()) {
            log.info("HBaseConfiguration设置了{}={}", entry.getKey(), entry.getValue());
            conf.set(entry.getKey(), entry.getValue());
        }

        Connection connection = ConnectionFactory.createConnection(conf);
        Admin admin = connection.getAdmin();

        Job job = Job.getInstance();
        Table table = connection.getTable(TableName.valueOf(tableName));
        RegionLocator regionLocator = connection.getRegionLocator(TableName.valueOf(tableName));
        job.setMapOutputKeyClass(ImmutableBytesWritable.class);
        job.setMapOutputValueClass(KeyValue.class);
        HFileOutputFormat2.configureIncrementalLoad(job, table, regionLocator);

        List<HRegionInfo> hRegionInfos = admin.getTableRegions(TableName.valueOf(tableName));
        if (hRegionInfos == null) {
            String msg = String.format("admin.getTableRegions(\"%s\")的结果为空，请检查该表是否存在。", tableName);
            throw new NullException(msg);
        }
        ArrayList<String> regionSplits = new ArrayList<>();
        for (HRegionInfo item : hRegionInfos) {
            regionSplits.add(new String(item.getEndKey()));
        }
        regionSplits.remove(regionSplits.size() - 1);
        log.info("HBase表{}的分区数组={}", tableName, regionSplits);

        if (regionSplits.size() > 0) {
            JavaPairRDD<ImmutableBytesWritable, KeyValue> repartitioned =
                    hFileRdd.repartitionAndSortWithinPartitions(new RegionPartitioner(regionSplits.toArray(new String[regionSplits.size()])));
            repartitioned.saveAsNewAPIHadoopFile(hFilePath, ImmutableBytesWritable.class, KeyValue.class,
                    HFileOutputFormat2.class, conf);
        } else {
            hFileRdd.saveAsNewAPIHadoopFile(hFilePath, ImmutableBytesWritable.class, KeyValue.class,
                    HFileOutputFormat2.class, conf);
        }
        log.info("hfile文件已经写完！在{}目录下！", hFilePath);

        if (isTruncate == 1) {
            // 如果写数据钱需要清除原有表中数据，则执行以下内容
            if (admin.isTableDisabled(TableName.valueOf(tableName))) {
                log.info("HBASE表{}已经处于disable状态，即将进行truncate！", tableName);
            } else {
                log.info("HBASE表{}已经处于disable状态，即将进行disable！", tableName);
                admin.disableTable(TableName.valueOf(tableName));
                log.info("HBASE表{}执行disable成功，即将进行truncate！", tableName);
            }
            if (isPreserve == 1) {
                admin.truncateTable(TableName.valueOf(tableName), true);
                log.info("HBASE表{}执行truncate成功, 保留原有分区！", tableName);
            } else {
                admin.truncateTable(TableName.valueOf(tableName), false);
                log.info("HBASE表{}执行truncate成功, 不保留原有分区！", tableName);
            }
        }

        log.info("开始使用bulk装{}的hfile文件！", hFilePath);
        LoadIncrementalHFiles bulkLoader = new LoadIncrementalHFiles(conf);
        bulkLoader.doBulkLoad(new Path(hFilePath), admin, table, regionLocator);

        log.info("sink sinkId={}成功", sink.getId());
    }

    public HashMap<String, String> parseHBaseConfig(String hBaseConfig) {
        /**
         * 解析HBaseConfiguration需要参数，格式为："hbase.mapreduce.bulkload.max.hfiles.perRegion.perFamily:3200,xxx.xxx.xx:323"
         * @param hBaseConfig : hBaseConfig
         * @author issac.young
         * @date 2021/5/12 9:14 上午
         * @return java.util.HashMap<java.lang.String, java.lang.String>
         */
        HashMap<String, String> map = new HashMap<>();
        if (StringUtils.isNotBlank(hBaseConfig)) {
            String[] keyValues = hBaseConfig.split(",");
            for (String item : keyValues) {
                String[] keyValue = item.split(":");
                map.put(keyValue[0], keyValue[1]);
            }
        }
        return map;
    }

    private class RegionPartitioner extends Partitioner {
        private String[] endKeys;
        private int numPartitions;

        public RegionPartitioner(String[] endKeys) {
            this.endKeys = endKeys;
            this.numPartitions = endKeys.length + 1;
        }

        @Override
        public int numPartitions() {
            return this.numPartitions;
        }

        @Override
        public int getPartition(Object key) {
            if (this.endKeys.length == 0) {
                // 如果这个hbase表没有分区信息，则所有数据都写到一个文件里面
                // 经测试，当前情况下，这个partition里面的数据不会进行排序，所以调用RegionPartitioner的时候就避免走到这一步
                return 0;
            } else {
                byte[] keyBytes = ((ImmutableBytesWritable) key).copyBytes();
                String comparedKey = new String(keyBytes).substring(0, endKeys[0].length());
                for (int i = 0; i < this.endKeys.length; i++) {
                    if (comparedKey.compareTo(endKeys[i]) < 0) {
                        return i;
                    }
                }
                return endKeys.length;
            }
        }
    }
}
