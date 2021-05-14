package com.issac.studio.app.handle;

import com.issac.studio.app.entity.domain.config.handle.SQLHandleConfig;
import com.issac.studio.app.entity.dto.ExternalParam;
import com.issac.studio.app.exception.NullException;
import com.issac.studio.app.exception.TypeException;
import com.issac.studio.app.param.ParamHandler;
import com.issac.studio.app.util.JdbcUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * @description: 执行SQL
 * @file: SQLHandle
 * @author: issac.young
 * @date: 2021/5/11 9:29 上午
 * @since: v1.0.0
 * @copyright (C), 1992-2021, issac
 */
public class SQLHandle extends Handle {
    private final static Logger log = LoggerFactory.getLogger(SQLHandle.class);

    @Override
    public void handle(com.issac.studio.app.entity.domain.Handle handle, ExternalParam eParam) throws Exception {
        SQLHandleConfig sqlHandleConfig;
        if (handle != null) {
            if (handle.getHandleConfigEntity() instanceof SQLHandleConfig) {
                sqlHandleConfig = (SQLHandleConfig) handle.getHandleConfigEntity();
            } else {
                String msg = String.format("handle描述实体类型异常！预期是{%s}, 实际是{%s}", SQLHandleConfig.class.getName(),
                        handle.getHandleConfigEntity().getClass().getName());
                throw new TypeException(msg);
            }
        } else {
            String msg = "传入的handle描述实体为null";
            throw new NullException(msg);
        }
        log.info("开始handle handleId={}的数据", handle.getId());

        Properties prop = new Properties();
        prop.put("user", sqlHandleConfig.getUser());
        prop.put("password", sqlHandleConfig.getPasswd());

        JdbcUtil jdbcUtil = new JdbcUtil(sqlHandleConfig.getDriverClass(), sqlHandleConfig.getUrl(), prop);
        String handledSQL = ParamHandler.handleSQL(sqlHandleConfig.getSql(), eParam.getParamDt());
        jdbcUtil.exec(handledSQL);

        log.info("完成处理handle！handleId={}", handle.getId());
    }
}
