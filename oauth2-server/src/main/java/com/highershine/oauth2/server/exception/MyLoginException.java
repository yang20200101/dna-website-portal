package com.highershine.oauth2.server.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @Description: 找不到用户异常
 * @Author: xueboren
 * @Date: 2020/12/10 14:48
 */
public class MyLoginException extends AuthenticationException {

    public MyLoginException(String msg) {
        super(msg);
    }

    public MyLoginException(String msg, Throwable t) {
        super(msg, t);
    }
}
