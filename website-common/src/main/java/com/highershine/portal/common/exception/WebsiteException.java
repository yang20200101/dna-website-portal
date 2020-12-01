package com.highershine.portal.common.exception;

import lombok.Data;

/**
 * @Description: 门户异常
 * @Author: mizhanlei
 * @Date: 2020/4/4 10:33
 */
@Data
public class WebsiteException extends RuntimeException {
    private String msg;

    public WebsiteException(String msg) {
        super(msg);
    }

}
