package com.highershine.portal.common.enums;

/**
 * @author highershine.mizhanlei
 * @date 2018/8/27 12:50
 */
public enum ResultEnum {

    /**
     * 成功
     */
    SUCCESS_STATUS(1, "操作成功！");

    private Integer code;
    private String msg;

    /**
     * 根据code 获取msg
     *
     * @param value
     * @return
     */
    public static String getMsgByCode(int value) {
        ResultEnum[] resultEnum = values();
        for (ResultEnum typeEnum : resultEnum) {
            if (typeEnum.getCode() == value) {
                return typeEnum.getMsg();
            }
        }
        return null;
    }

    ResultEnum(Integer code, String msg) {
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
