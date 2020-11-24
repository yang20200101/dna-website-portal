package com.highershine.portal.common.enums;

import lombok.Getter;

/**
 * @Description
 * @Author zxk
 * @Date 2020/4/17 10:55
 **/
@Getter
public enum PolicyTypeEnum {
    READ("read", "只读"),
    WRITE("write", "只写"),
    READ_WRITE("read_write", "读写");

    PolicyTypeEnum(String type, String policy) {
        this.type = type;
        this.policy = policy;
    }
    private String type;
    private String policy;

}
