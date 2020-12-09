package com.highershine.oauth2.server.entity;

import lombok.Data;

/**
 * @param <T>
 * @author highershine.mizhanlei
 * @date 2018/8/27 13:02
 */
@Data
public class Result<T> {
    private Integer code;

    private String msg;

    private T data;
}
