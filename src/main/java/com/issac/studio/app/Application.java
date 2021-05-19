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

    private static Date startTime = new Date(); // 任务开始执行的时间

    public static void main(String[] args) {
        if (args.length != 2) {
            log.error("参数错误！参数数量不等于2！args={}", JSONObject.toJSONString(args));
            return;
        }
        log.info("开始执行！args={}", JSONObject.toJSONString(args));

        String taskKey = args[0];
        String paramDt = args[1];
        ExternalParam eParam = new ExternalParam(paramDt);
        Task task;
        List<Source> sources;
        List<Sink> sinks;
        List<Handle> preHandles;
        List<Handle> afterHandles;
        List<Handle> exceptionHandles = null;
        SparkSession sparkSession = null;

        try {
            task = getTask(taskKey);
            switch (task.getLatch()){
                case 1:
                    sources = getSources(task.getId());
                    sinks = getSinks(task.getId());
                    preHandles = getPreHandle(task.getId());
                    afterHandles = getAfterHandle(task.getId());
                    exceptionHandles = getExceptionHandle(task.getId());

                    if(!preHandles.isEmpty()){
                        log.info("发现pre类型的handle，现在开始执行该handle！");
                        execHandles(preHandles, eParam);
                    }

                    sparkSession = buildSparkSession(task, startTime);

                    buildSource(sparkSession, sources, eParam);
                    Dataset<Row> resultDS = buildTransform(sparkSession, task.getTransformSql(), eParam);
                    sink(resultDS, sinks);

                    if(!afterHandles.isEmpty()){
                        log.info("发现after类型的handle，现在开始执行该handle！");
                        execHandles(afterHandles, eParam);
                    }

                    log.info("执行过程无异常！");
                    clean(taskKey, sparkSession);

                    insetTaskLog(taskKey, 1, null);
                    System.exit(0);
                    break;
                case 2:
                    log.info("task={}，检测到latch=2，跳过该任务运行！", JSONObject.toJSONString(task));
                    insetTaskLog(taskKey, 4, null);
                    break;
            }
        } catch (Exception e) {
            if(e instanceof HandleException){
                log.error("handle执行过程发生异常，error=", e);
                insetTaskLog(taskKey, 3, e);
            }else {
                log.error("主流程执行过程中发生异常，error=", e);
            }

            if(exceptionHandles != null && !exceptionHandles.isEmpty()){
                log.info("发现exception类型的handle，现在开始执行该handle！");
                try {
                    execHandles(exceptionHandles, eParam);
                }catch (Exception e1){
                    log.error("exception类型的handle执行异常！error=", e1);
                    insetTaskLog(taskKey, 3, e1);
                }
            }

            clean(taskKey, sparkSession);

            insetTaskLog(taskKey, 2, e);
            System.exit(-1);
        }

    }

    private static void insetTaskLog(String taskKey, Integer status, Exception e){
        try {
            TaskLog taskLog = new TaskLog();
            taskLog.setTaskKey(taskKey);
            taskLog.setStartTime(startTime);
            taskLog.setEndTime(new Date());
            taskLog.setStatus(status);
            if(e != null){
                taskLog.setErrorLog(ExceptionUtils.getStackTrace(e));
            }
            taskLog.setCreated(new Date());
            taskLog.setModified(new Date());
            taskLog.setYn(1);

            TaskLogMapper mapper = Persistent.getMapper(TaskLogMapper.class);
            mapper.insert(taskLog);
        }catch (Exception e1){
            log.info("写日志步骤发生异常，异常日志无法记录到数据库中！error=", e1);
        }
    }

    private static void clean(String taskKey, SparkSession session){
        if(session != null){
            try{
                session.stop();
            }catch (Exception e){
                log.error("关闭sparkSession出错，请排查原因！error=", e);
                insetTaskLog(taskKey, 2, e);
                return;
            }
        }
        log.info("关闭sparkSession链接");
    }

    private static void execHandles(List<Handle> handles, ExternalParam eParam) throws Exception{
        for(Handle item: handles){
            try {
                Class<?> configClass = Class.forName(item.getHandleConfigType());
                AbstractHandleConfig handleConfig = (AbstractHandleConfig)JSONObject.parseObject(item.getHandleConfigJson(), configClass);
                item.setHandleConfigEntity(handleConfig);

                com.issac.studio.app.handle.Handle handle = (com.issac.studio.app.handle.Handle)Class.forName(item.getHandleType()).newInstance();
                handle.handle(item, eParam);
            }catch (Exception e){
                String msg = String.format("handle:{%s}执行异常！e={%s}", JSONObject.toJSONString(item), ExceptionUtils.getStackTrace(e));
                throw new HandleException(msg);
            }
        }
    }

    private static List<Handle> getExceptionHandle(Long taskId){
        HandleMapper mapper = Persistent.getMapper(HandleMapper.class);
        return mapper.query(new Handle(taskId, 3));
    }

    private static List<Handle> getAfterHandle(Long taskId){
        HandleMapper mapper = Persistent.getMapper(HandleMapper.class);
        return mapper.query(new Handle(taskId, 2));
    }

    private static List<Handle> getPreHandle(Long taskId){
        HandleMapper mapper = Persistent.getMapper(HandleMapper.class);
        return mapper.query(new Handle(taskId, 1));
    }

    /**
     * 根据配置创建sparkSession对象
     * @param task : task
     * @param startTime : startTime
     * @author issac.young
     * @date 2020/12/10 3:53 下午
     * @return org.apache.spark.sql.SparkSession
     */
    private static SparkSession buildSparkSession(Task task, Date startTime){
        log.info("开始build sparkSession！");

        String appName = "sqlBase-" + task.getTaskKey() + "-" + startTime.getTime();
        JSONObject jsonObject = JSONObject.parseObject(task.getSparkConfig());
        SparkConf sparkConf = new SparkConf();
        for(Map.Entry<String, Object> entry : jsonObject.entrySet()){
            sparkConf.set(entry.getKey(), String.valueOf(entry.getValue()));
        }

        SparkSession.Builder builder = SparkSession
                .builder()
                .appName(appName);

        if (task.getHiveSupport() == 1) {
            log.info("过程需要使用hive，打开enableHiveSupport！");
            builder = builder.enableHiveSupport();
        }

        SparkSession sparkSession = builder.config(sparkConf).getOrCreate();

        log.info("SparkSession的配置如下：{}", JSONObject.toJSONString(sparkSession.sessionState().conf().settings()));
        return sparkSession;
    }

    /**
     * 利用反射创建所有的sparkSQL临时表
     *
     * @param session : session
     * @param sources : readers
     * @return void
     * @author issac.young
     * @date 2020/12/4 2:36 下午
     */
    private static void buildSource(SparkSession session, List<Source> sources, ExternalParam eParam) throws Exception {
        for (Source item : sources) {
            Class<?> configClass = Class.forName(item.getSourceConfigType());
            AbstractSourceConfig sourceConfig = (AbstractSourceConfig) JSONObject.parseObject(item.getSourceConfigJson(), configClass);
            item.setSourceConfigEntity(sourceConfig);

            com.issac.studio.app.source.Source source = (com.issac.studio.app.source.Source) Class.forName(item.getSourceType()).newInstance();
            source.handleSource(session, item, eParam);
        }
    }

    /**
     * 执行etl
     *
     * @param session   : session
     * @param transform : transform
     * @return org.apache.spark.sql.Dataset<org.apache.spark.sql.Row>
     * @author issac.young
     * @date 2020/12/4 2:35 下午
     */
    private static Dataset<Row> buildTransform(SparkSession session, String transform, ExternalParam eParam) throws Exception {
        return Transform.execute(session, transform, eParam);
    }

    /**
     * 利用反射创建sink对象
     *
     * @param ds      : ds
     * @param sinks : writers
     * @return void
     * @author issac.young
     * @date 2020/12/4 2:34 下午
     */
    private static void sink(Dataset<Row> ds, List<Sink> sinks) throws Exception {
        for (Sink item : sinks) {
            Class<?> configClass = Class.forName(item.getSinkType());
            AbstractSinkConfig writerConfig = (AbstractSinkConfig) JSONObject.parseObject(item.getSinkConfigJson(), configClass);
            item.setSinkConfigEntity(writerConfig);

            com.issac.studio.app.sink.Sink sink = (com.issac.studio.app.sink.Sink) Class.forName(item.getSinkType()).newInstance();
            sink.sink(ds, item);
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
    private static Task getTask(String taskKey) throws Exception{
        TaskMapper taskMapper = Persistent.getMapper(TaskMapper.class);
        Task task = new Task();
        task.setTaskKey(taskKey);
        task.setYn(1);
        List<Task> tasks = taskMapper.query(task);
        if (tasks == null || tasks.size() == 0) {
            throw new NotFoundException(String.format("未在数据库中国找到该任务！taskKey={%s}", taskKey));
        }
        return tasks.get(0);
    }

    /**
     * 根据任务id获取这个任务所有的sink
     *
     * @param taskId : taskId
     * @return java.util.List<com.issac.studio.app.entity.domain.Writer>
     * @author issac.young
     * @date 2020/12/4 2:33 下午
     */
    private static List<Sink> getSinks(Long taskId) throws Exception{
        SinkMapper sinkMapper = Persistent.getMapper(SinkMapper.class);
        List<Sink> sinks = sinkMapper.query(new Sink(taskId));
        if (sinks == null || sinks.size() == 0) {
            throw new NotFoundException(String.format("getSinks的到了null值！taskKey={%s}", taskId));
        }
        return sinks;
    }

    /**
     * 根据任务id获取这个任务所有的source
     *
     * @param taskId : taskId
     * @return java.util.List<com.issac.studio.app.entity.domain.Reader>
     * @author issac.young
     * @date 2020/12/4 2:34 下午
     */
    private static List<Source> getSources(Long taskId) throws Exception{
        SourceMapper sourceMapper = Persistent.getMapper(SourceMapper.class);
        List<Source> sources = sourceMapper.query(new Source(taskId));
        if (sources == null || sources.size() == 0) {
            throw new NotFoundException(String.format("getSinks的到了null值！taskKey={%s}", taskId));
        }
        return sources;
    }
}
