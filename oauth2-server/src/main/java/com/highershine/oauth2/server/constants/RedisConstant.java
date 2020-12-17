package com.highershine.oauth2.server.constants;

/**
 * redis常量
 *
 * @author mizhanlei
 */
public final class RedisConstant {
    private RedisConstant() {

    }

    // redis存放根目录
    public static final String ROOT_DIR = "oauth2:";

    // login登录信息存放目录
    public static final String LOGIN_DIR = "login:";

    // login登录信息存放目录
    public static final String REDIS_LOGIN = ROOT_DIR + LOGIN_DIR;

}
