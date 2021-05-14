package com.issac.studio.app.entity.dto;

import java.io.Serializable;

/**
 * @description: 在实体间传递的额外参数
 * @file: ExternalParam
 * @author: issac.young
 * @date: 2021/5/11 9:17 上午
 * @since: v1.0.0
 * @copyright (C), 1992-2021, issac
 */
public class ExternalParam implements Serializable {
    private String paramDt;

    public ExternalParam() {
    }

    public ExternalParam(String paramDt) {
        this.paramDt = paramDt;
    }

    public String getParamDt() {
        return paramDt;
    }

    public void setParamDt(String paramDt) {
        this.paramDt = paramDt;
    }
}
