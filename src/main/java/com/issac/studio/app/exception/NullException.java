package com.issac.studio.app.exception;

/**
 * @description: 空类型错误
 * @file: NullException
 * @author: issac.young
 * @date: 2020/12/1 10:56 上午
 * @since: v1.0.0
 * @copyright (C), 1992-2020, issac
 */
public class NullException extends RuntimeException{
    public NullException(String message) {
        super(message);
    }
}
