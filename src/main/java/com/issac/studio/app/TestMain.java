package com.issac.studio.app;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;

import java.util.Properties;

/**
 * @description: 用于测试的主类
 * @file: TestMain
 * @author: issac.young
 * @date: 2020/12/2 4:09 下午
 * @since: v1.0.0
 * @copyright (C), 1992-2020, issac
 */
public class TestMain {
    public static void main(String[] args) throws Exception {
        /** 
         * 
         * @param args : args 
         * @author issac.young
         * @date 2020/12/25 12:36 下午 
         * @return void 
         */
//        InputStream resourceAsStream = Resources.getResourceAsStream("mybatis/mybatis.xml");
//        SqlSessionFactory build = new SqlSessionFactoryBuilder().build(resourceAsStream);
//        SqlSession openSession = build.openSession(true);
//        TaskMapper mapper = openSession.getMapper(TaskMapper.class);
//        Task task = new Task();
////        task.setId(4L);
//        task.setTaskKey("myKey1206");
//        task.setTaskName("myName");
//        task.setCreated(new Date());
//        task.setModified(new Date());
//        task.setYn(1);
//        task.setTransformSql("mySQL");
//        Long insert = mapper.insert(task);
//        Task task1 = mapper.queryById(4L);
//        System.out.println(JSONObject.toJSONString(insert));

//        ArrayList<String> strings = new ArrayList<>();
//        strings.add(null);
//        strings.add("s");
//        strings.add("null");
//        System.out.println(StringUtils.join(strings));

//        String jj = "你好";
//        System.out.println(jj.length());

//        SparkSession session = SparkSession
//                .builder()
//                .appName("my-app-name")
//                .master("local[3]")
////                .enableHiveSupport()
//                .getOrCreate();
//        Dataset<Row> ds = session.read().format("jdbc")
//                .option("url", "jdbc:mysql://127.0.0.1:3306/irmp?serverTimezone=GMT%2B8&useSSL=false")
//                .option("dbtable", "task")
//                .option("user", "root")
//                .option("password", "123456")
//                .option("partitionColumn", "id")
//                .option("lowerBound", 0)
//                .option("upperBound", 100)
//                .option("numPartitions", 2)
//                .load();
//
//        ds.write().format("jdbc")
//                .option("url", "jdbc:mysql://127.0.0.1:3306/irmp?serverTimezone=GMT%2B8&useSSL=false")
//                .option("dbtable", "task1")
//                .option("user", "root")
//                .option("password", "123456")
//                .mode(SaveMode.Append)
//                .save();

//        SparkSession session = SparkSession
//                .builder()
//                .appName("my-app-name")
//                .master("local[3]")
////                .enableHiveSupport()
//                .getOrCreate();
        SparkConf sparkConf = new SparkConf();
        sparkConf.set("spark.master", "local[3]");
        sparkConf.set("spark.app.name", "my-name-app");
        SparkSession session = SparkSession.builder()
                .config(sparkConf)
//                .appName("muu-name-app")
                .getOrCreate();

        Dataset<Row> ds = session.read().format("jdbc")
                .option("url", "jdbc:oracle:thin:@127.0.0.1:1521:XE")
                .option("driver", "oracle.jdbc.driver.OracleDriver")
                .option("dbtable", "task")
                .option("user", "devuser")
                .option("password", "123456")
                .option("partitionColumn", "id")
                .option("lowerBound", 0)
                .option("upperBound", 100)
                .option("numPartitions", 2)
                .load();


        Properties properties = new Properties();
        properties.put("driver", "oracle.jdbc.driver.OracleDriver");
        properties.put("user", "devuser");
        properties.put("password", "123456");
//        Thread.sleep(3203203020302302L);
        ds.write()
                .mode(SaveMode.Append)
                .jdbc("jdbc:oracle:thin:@127.0.0.1:1521:XE", "task1", properties);

    }
}
