<h1 align="center" style="margin: 30px 0 30px; font-weight: bold;">data-hub v1.0-SNAPSHOT</h1>
<h4 align="center">用SQL的方式实现离线多数据源混合计算和互导！</h4>

---


## data-hub是什么？
data-hub是一个能接入多种数据源，使用SparkSQL作为通道，最终输出到多数据源的jar包，主要实现：多数据源数据使用SQL的方式进行计算，并将计算结果导入到多种数据源

特点：
1. **简单** ：按照下文方法简单调用即可使用，如果你有任务调度平台，使用起来同样简单
2. **强大** ：目前不仅支持在大数据集群中使用，还是支持本地运行，读取结构化数据时支持自定义换行符；目前支持的数据源：jdbc、hive、hdfs-file、local-file，输出数据源：jdbc、hive、HBASE；同时支持spark参数配置
4. **高扩展** ：输入数据源、输出数据源使用插件化代码，只需继承插件父类并实现对应方法即可，无需修改主流程


## 部署

data-hub的使用非常简单，有多简单呢？你只需要： 在执行之前你需要3个简单的步骤：

1、在MySQL数据库中执行ddl.sql文件中的语句，将该MySQL的域名或者ip地址配置到prod.properties文件中

2、打包项目成一个jar包：
```shell script
mvn clean package -D maven.test.skip=true -P prod
```
3、将jar包、DATA_HUB.sh、DATA_HUB_LOCAL.sh(这个可以不放到driver上)放到大数据driver机器的 /home/xxuser/xxx/ 目录下

4、执行以下命令
``` shell script
sh DATA_HUB.sh $task_key $20210514
```

## demo使用
1、执行【部署】过程中的【步骤1】后，在demo.sql文件中有demo演示使用的数据，执行这些insert语句

2、按照【部署】过程的【步骤2】【步骤3】【步骤4】执行，即可看到效果


如果不小心发现了bug，热烈欢迎给项目提交pr，手动笑脸！！！

## 联系方式
邮箱：issac.young@qq.com

