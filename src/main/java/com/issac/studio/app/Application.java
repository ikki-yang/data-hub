package com.issac.studio.app;

import com.alibaba.fastjson.JSONObject;
import com.issac.studio.app.entity.domain.*;
import com.issac.studio.app.entity.domain.config.handle.AbstractHandleConfig;
import com.issac.studio.app.entity.domain.config.sink.AbstractSinkConfig;
import com.issac.studio.app.entity.domain.config.source.AbstractSourceConfig;
import com.issac.studio.app.entity.dto.ExternalParam;
import com.issac.studio.app.entity.mapper.*;
import com.issac.studio.app.exception.HandleException;
import com.issac.studio.app.exception.NotFoundException;
import com.issac.studio.app.persistent.Persistent;
import com.issac.studio.app.transform.Transform;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class Application {
    private final static Logger log = LoggerFactory.getLogger(Application.class);


    public static void main(String[] args) {
        if (args.length != 2) {
            log.error("参数错误！参数数量不等于2！args={}", JSONObject.toJSONString(args));
            return;
        }
        log.info("开始执行！args={}", JSONObject.toJSONString(args));

        String taskKey = args[0];
        String paramDt = args[1];
        Task task = getTask(taskKey);


    }

    /**
     * 根据taskKey获取任务信息
     *
     * @param taskKey : taskKey
     * @return com.issac.studio.app.entity.domain.Task
     * @author issac.young
     * @date 2020/12/3 9:22 上午
     */
    private static Task getTask(String taskKey) {
        TaskMapper taskMapper = Persistent.getMapper(TaskMapper.class);
        Task task = new Task();
        task.setTaskKey(taskKey);
        task.setYn(1);
        List<Task> tasks = taskMapper.query(task);
        if (tasks == null || tasks.size() == 0) {
            throw new NotFoundException(String.format("未在数据库中找到该任务！taskKey={%s}", taskKey));
        }
        return tasks.get(0);
    }
}
