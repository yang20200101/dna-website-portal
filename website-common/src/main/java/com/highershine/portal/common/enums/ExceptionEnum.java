package com.highershine.portal.common.enums;

/**
 * @author highershine.mizhanlei
 * @date 2018/8/27 12:50
 */
public enum ExceptionEnum {

    UNKNOWN_EXCEPTION(-1, "未知异常"),
    ERROR_PARAMETERS(-2, "参数异常"),
    EXISTS_PARAMETERS(-3, "重复录入;"),
    NOT_FIND_DATA(-4, "没有查询到相关数据"),
    SETTING_TIME_REPEAT(-5, "该配置与其他时间区间存在交集");

    private Integer code;

    private String msg;

    /**
     * 根据code 获取msg
     *
     * @param value
     * @return
     */
    public static String getMsgByCode(int value) {
        ExceptionEnum[] exceptionEnum = values();
        for (ExceptionEnum typeEnum : exceptionEnum) {
            if (typeEnum.getCode() == value) {
                return typeEnum.getMsg();
            }
        }
        return null;
    }

    ExceptionEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
