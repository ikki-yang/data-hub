package com.issac.studio.app;

import com.alibaba.fastjson.JSONObject;
import com.issac.studio.app.entity.domain.Task;
import com.issac.studio.app.entity.mapper.TaskMapper;
import com.issac.studio.app.exception.NotFoundException;
import com.issac.studio.app.persistent.Persistent;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class SparkSubmitter {
    private final static Logger log = LoggerFactory.getLogger(SparkSubmitter.class);


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

        JSONObject configJson = JSONObject.parseObject(sparkConfig);
        String master = configJson.getString("master");

        StringBuilder commandToExec = new StringBuilder();
        if (master.startsWith("local")) {
            commandToExec.append("java -cp ")
                    .append(jarPath)
                    .append(" ")
                    .append("com.issac.studio.app.SparkMain ");
        } else {
            commandToExec.append("spark-submit --class com.issac.studio.app.SparkMain ");
            for (Map.Entry<String, Object> entry : configJson.entrySet()) {
                String key = entry.getKey();
                String value = String.valueOf(entry.getValue());
                commandToExec.append("--")
                        .append(key)
                        .append(" ")
                        .append(value)
                        .append(" ");
            }
            commandToExec.append(jarPath)
                    .append(" ");
        }

        commandToExec.append(taskKey)
                .append(" ")
                .append(paramDt);

        log.info("即将执行的cmd命令：{}", commandToExec);
        CommandLine cmd = CommandLine.parse(commandToExec.toString());
        DefaultExecutor executor = new DefaultExecutor();
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        executor.setStreamHandler(new PumpStreamHandler(byteArrayOS, byteArrayOS));
        try {
            int exitValue = executor.execute(cmd);

            String retStr = byteArrayOS.toString().trim();
            log.info("获取到执行SparkSubmitter返回的stream: {}", retStr);
            byteArrayOS.close();
            log.info("已经关闭执行byteArrayOS返回的stream！");

            if (exitValue != 0) {
                log.error("命令未执行完成就推出了程序！ exit value={}", exitValue);
            } else {
                log.info("命令执行完成！ exit value={}", exitValue);
            }
            System.exit(0);
        } catch (Exception e) {
            log.error("命令执行过程报错，error=", e);
            log.error("即将获取报错byteArrayOS返回的stream！");
            String retStr = byteArrayOS.toString().trim();
            log.info("报错byteArrayOS返回的stream：\n{}", retStr);
            try {
                byteArrayOS.close();
            } catch (IOException ioException) {
                log.error("关闭报错byteArrayOS过程异常，e=", ioException);
                System.exit(-1);
            }
            log.error("正常关闭报错byteArrayOS返回的stream！");
            System.exit(-1);
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
