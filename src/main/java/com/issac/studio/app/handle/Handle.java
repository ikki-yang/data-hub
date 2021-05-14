package com.issac.studio.app.handle;

import com.issac.studio.app.entity.dto.ExternalParam;

import java.io.Serializable;

/**
 * @description: handle的抽象类
 * @file: Handle
 * @author: issac.young
 * @date: 2021/5/11 10:50 上午
 * @since: v1.0.0
 * @copyright (C), 1992-2021, issac
 */
public abstract class Handle implements Serializable {
    /**
     * 根据具体的handle连的内容处理
     * @param handle : handle
     * @param eParam : eParam
     * @author issac.young
     * @date 2021/5/11 10:58 上午
     * @return void
     */
    abstract public void handle(com.issac.studio.app.entity.domain.Handle handle, ExternalParam eParam) throws Exception;
}
