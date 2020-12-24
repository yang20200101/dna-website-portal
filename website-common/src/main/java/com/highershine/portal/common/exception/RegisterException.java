package com.highershine.portal.common.exception;

import lombok.Data;

/**
 * @Description: 注册异常
 * @Author: xueboren
 * @Date: 2020/12/24 10:43
 */
@Data
public class RegisterException extends RuntimeException {
    private String msg;

    public RegisterException(String msg) {
        super(msg);
    }

}
