package com.issac.studio.app;

import com.alibaba.fastjson.JSONObject;
import com.issac.studio.app.entity.domain.Task;
import com.issac.studio.app.entity.mapper.TaskMapper;
import com.issac.studio.app.exception.NotFoundException;
import com.issac.studio.app.persistent.Persistent;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

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
        String jarPath = task.getJarPath();
        String sparkConfig = task.getSparkConfig();

        StringBuilder commandToExec = new StringBuilder();
        commandToExec.append("spark-submit --class com.issac.studio.app.Application ");
        commandToExec.append(jarPath + " ");


        CommandLine cmd = CommandLine.parse(commandToExec.toString());
        DefaultExecutor executor = new DefaultExecutor();
        try {
            int exitValue = executor.execute(cmd);
            if (exitValue != 0) {
                log.info("命令未执行完成就推出了程序！ exit value={}", exitValue);
            } else {
                log.info("命令执行完成！ exit value={}", exitValue);
            }
        } catch (Exception e) {
            log.error("命令执行过程报错，错误不向外抛出，error=", e);
        }

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
