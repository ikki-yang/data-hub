#!/bin/sh -xf

export HADOOP_HOME=/opt/CDH/parcels/hadoop
export LD_LIBRARY_PATH=$HADOOP_HOME/lib/native

#参数1：任务key；参数2：跑任务的日期yyyyMMdd
task_key=$1
date=$2

java -cp /home/xxuser/xxx/data-hub.jar --class com.issac.studio.app.SparkSubmitter ${task_key} ${date}