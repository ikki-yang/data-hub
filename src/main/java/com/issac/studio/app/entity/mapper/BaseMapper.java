package com.issac.studio.app.entity.mapper;

import java.util.List;

/**
 * @description: 基础实体操作dao
 * @file: BaseMapper
 * @author: issac.young
 * @date: 2020/12/2 2:41 下午
 * @since: v1.0.0
 * @copyright (C), 1992-2020, issac
 */
public interface BaseMapper<T> {
    List<T> query(T query);
    T queryById(Long id);
    void updateById(T entity);
    Long insert(T entity);
}
