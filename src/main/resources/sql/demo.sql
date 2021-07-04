INSERT INTO data_hub.task (id, task_key, task_name, jar_path, transform_sql, hive_support, spark_config, latch, created, modified, yn) VALUES (1, 'hdfs2hbase_cluster', '集群模式从jdbc导数据到hive中', '/home/issac/target/data-hub.jar', 'select
	student_id	as rowKey
	, student_name as `cf:student_name`
	, student_greade as `cf:student_grade`
from
	my_temp_view', 0, '{"master":"yarn", "executor-cores":2, "driver-memory":"1g"}', 1, '2021-07-02 08:50:31', '2021-07-02 08:50:37', 1);
INSERT INTO data_hub.task (id, task_key, task_name, jar_path, transform_sql, hive_support, spark_config, latch, created, modified, yn) VALUES (2, 'file2jdbc_local', '本地模式从jdbc导数据到hive中', '/Users/issac/IdeaProjects/data-hub/target/data-hub.jar', 'select * from my_temp_view', 0, '{"spark.master":"local[2]", "spark.executor.cores":2, "spark.driver.memory":"1g"}', 1, '2021-07-02 08:50:31', '2021-07-02 08:50:37', 1);



INSERT INTO data_hub.source (id, task_id, temp_view_name, source_type, source_config_type, source_config_json, created, modified, yn) VALUES (1, 1, 'my_temp_view', 'com.issac.studio.app.source.FileSource', 'com.issac.studio.app.entity.domain.config.source.FileSourceConfig', '{"location": "/hdfs/data/source/first_test_#{dt}", "fields": [{"name":"student_grade", "type": "string"}, {"name":"student_name", "type": "string"}, {"name":"student_id", "type": "long"}], "separator": ","}', '2021-07-03 13:05:53', '2021-07-03 13:05:54', 1);
INSERT INTO data_hub.source (id, task_id, temp_view_name, source_type, source_config_type, source_config_json, created, modified, yn) VALUES (2, 2, 'my_temp_view', 'com.issac.studio.app.source.FileSource', 'com.issac.studio.app.entity.domain.config.source.FileSourceConfig', '{"location": "/Users/issac/IdeaProjects/data-hub/resources/test_data/first_test_#{dt}", "fields": [{"name":"student_grade", "type": "string"}, {"name":"student_name", "type": "string"}, {"name":"student_id", "type": "long"}], "separator": ","}', '2021-07-03 13:05:53', '2021-07-03 13:05:54', 1);


INSERT INTO data_hub.sink (id, task_id, sink_type, sink_config_type, sink_config_json, created, modified, yn) VALUES (1, 1, 'com.issac.studio.app.sink.HBaseSink', 'com.issac.studio.app.entity.domain.config.sink.HBaseSinkConfig', '{"targetTable": "ISSAC:ACDM_STUDENT", "hFilePath": "/hdfs/data/hfiles", "isTruncate": "1", "isPreserve": "1", "hBaseConfig": "hbase.mapreduce.bulkload.max.hfiles.perRegion.perFamily:3200,xxx.xxx.xx:323"}', '2021-07-03 13:09:43', '2021-07-03 13:09:44', 1);
INSERT INTO data_hub.sink (id, task_id, sink_type, sink_config_type, sink_config_json, created, modified, yn) VALUES (2, 2, 'com.issac.studio.app.sink.JdbcSink', 'com.issac.studio.app.entity.domain.config.sink.JdbcSinkConfig', '{"url": "jdbc:mysql://localhost:3306/data_hub?characterEncoding=utf8", "driverClass": "com.mysql.cj.jdbc.Driver", "tableName": "test_student", "user": "root", "passwd": "123456", "partitionNum": "2", "saveMode": "append"}', '2021-07-03 13:09:43', '2021-07-03 13:09:44', 1);