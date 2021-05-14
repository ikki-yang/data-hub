package com.issac.studio.app.exception;

/**
 * @description: 长度异常
 * @file: LengthException
 * @author: issac.young
 * @date: 2020/12/1 11:28 上午
 * @since: v1.0.0
 * @copyright (C), 1992-2020, issac
 */
public class LengthException extends RuntimeException{
    public LengthException(String message) {
        super(message);
    }
}
