package com.issac.studio.app.handle;

import com.issac.studio.app.entity.domain.config.handle.CmdHandleConfig;
import com.issac.studio.app.entity.dto.ExternalParam;
import com.issac.studio.app.exception.NullException;
import com.issac.studio.app.exception.TypeException;
import com.issac.studio.app.param.ParamHandler;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description: 执行cmd命令
 * @file: CmdHandle
 * @author: issac.young
 * @date: 2021/5/11 9:28 上午
 * @since: v1.0.0
 * @copyright (C), 1992-2021, issac
 */
public class CmdHandle extends Handle {
    private final static Logger log = LoggerFactory.getLogger(CmdHandle.class);

    @Override
    public void handle(com.issac.studio.app.entity.domain.Handle handle, ExternalParam eParam) throws Exception {
        CmdHandleConfig cmdHandleConfig;
        if (handle != null) {
            if (handle.getHandleConfigEntity() instanceof CmdHandleConfig) {
                cmdHandleConfig = (CmdHandleConfig) handle.getHandleConfigEntity();
            } else {
                String msg = String.format("handle描述实体类型异常！预期是{%s}, 实际是{%s}", CmdHandleConfig.class.getName(),
                        handle.getHandleConfigEntity().getClass().getName());
                throw new TypeException(msg);
            }
        } else {
            String msg = "传入的handle描述实体为null";
            throw new NullException(msg);
        }
        log.info("开始handle handleId={}的数据", handle.getId());

        String[] commands = {};
        try {
            commands = cmdHandleConfig.getCommand().split("@#@");
        } catch (Exception e) {
            log.error("解析命令过程报错，错误不向外抛出，error=", e);
        }

        try {
            for (String command : commands) {
                String commandToExec = ParamHandler.handlePath(command, eParam.getParamDt());
                CommandLine cmd = CommandLine.parse(commandToExec);
                DefaultExecutor executor = new DefaultExecutor();
                ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
                executor.setStreamHandler(new PumpStreamHandler(byteArrayOS, byteArrayOS));
                try {
                    int exitValue = executor.execute(cmd);

                    String retStr = byteArrayOS.toString().trim();
                    log.info("获取到执行cmd返回的stream: {}", retStr);
                    byteArrayOS.close();
                    log.info("已经关闭执行cmd返回的stream！");

                    if (exitValue != 0) {
                        log.info("命令未执行完成就推出了程序！ exit value={}", exitValue);
                    } else {
                        log.info("命令执行完成！ exit value={}", exitValue);
                    }
                } catch (Exception e) {
                    log.error("命令执行过程报错，错误不向外抛出，error=", e);
                    String retStr = byteArrayOS.toString().trim();
                    log.info("报错cmd返回的stream：{}", retStr);
                    byteArrayOS.close();
                    log.info("已经关闭报错cmd返回的stream！");
                }
            }
        } catch (Exception e) {
            log.error("命令准备过程报错，错误不向外抛出，error=", e);
        }

        log.info("完成处理handle！handleId={}", handle.getId());
    }
}
