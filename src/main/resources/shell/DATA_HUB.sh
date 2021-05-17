#!/bin/sh -xf

export HADOOP_HOME=/opt/CDH/parcels/hadoop
export LD_LIBRARY_PATH=$HADOOP_HOME/lib/native

#参数1：任务key；参数2：跑任务的日期yyyyMMdd
task_key=$1
date=$2

spark-submit \
    --class com.issac.studio.app.Application \
    /home/xxuser/xxx/data-hub.jar ${task_key} ${date}