package com.issac.studio.app.exception;

/**
 * @description: handle执行异常
 * @file: HandleException
 * @author: issac.young
 * @date: 2021/5/11 9:27 上午
 * @since: v1.0.0
 * @copyright (C), 1992-2021, issac
 */
public class HandleException extends RuntimeException {
    public HandleException(String message) {
        super(message);
    }
}
