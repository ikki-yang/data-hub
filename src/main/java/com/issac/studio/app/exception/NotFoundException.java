package com.issac.studio.app.exception;

/**
 * @description: 未找到执行东西错误
 * @file: NotFoundException
 * @author: issac.young
 * @date: 2021/5/11 9:27 上午
 * @since: v1.0.0
 * @copyright (C), 1992-2021, issac
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
