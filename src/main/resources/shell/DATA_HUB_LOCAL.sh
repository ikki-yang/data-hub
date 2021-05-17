#!/bin/sh -xf

#参数1：任务key；参数2：跑任务的日期yyyyMMdd
task_key=$1
date=$2

java -DTASK_KEY -DPARAM_DT \
    -cp /home/xxuser/xxx/data-hub.jar com.issac.studio.app.Application ${task_key} ${date}