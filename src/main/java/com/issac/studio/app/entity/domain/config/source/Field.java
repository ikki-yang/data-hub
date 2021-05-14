package com.issac.studio.app.entity.domain.config.source;

import java.io.Serializable;

/**
 * @description: 表头的名字和雷兴国
 * @file: Field
 * @author: issac.young
 * @date: 2021/5/11 9:19 上午
 * @since: v1.0.0
 * @copyright (C), 1992-2021, issac
 */
public class Field implements Serializable {
    private String name;
    private String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
