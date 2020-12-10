package com.highershine.oauth2.server.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @Description: 找不到用户异常
 * @Author: xueboren
 * @Date: 2020/12/10 14:48
 */
public class MyUsernameNotFoundException extends AuthenticationException {

    public MyUsernameNotFoundException(String msg) {
        super(msg);
    }

    public MyUsernameNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }
}
