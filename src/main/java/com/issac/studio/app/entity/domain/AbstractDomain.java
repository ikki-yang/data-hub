package com.issac.studio.app.entity.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * @description: 数据库表抽象实体，用于存放公共信息
 * @file: AbstractDomain
 * @author: issac.young
 * @date: 2021/5/11 10:15 上午
 * @since: v1.0.0
 * @copyright (C), 1992-2021, issac
 */
public class AbstractDomain implements Serializable {
    public Long id;
    public Date created;
    public Date modified;
    public Integer yn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public Integer getYn() {
        return yn;
    }

    public void setYn(Integer yn) {
        this.yn = yn;
    }
}
