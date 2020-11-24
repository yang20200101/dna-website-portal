package com.highershine.portal.common.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @param <T>
 * @author highershine.mizhanlei
 * @date 2018/8/27 13:02
 */
@Data
@ApiModel("响应结果")
public class Result<T> {
    @ApiModelProperty(value = "接口返回状态码;1成功;-1失败;-2参数异常", example = "1", position = 0)
    private Integer code;

    @ApiModelProperty(value = "接口返回信息", example = "操作成功！", position = 1)
    private String msg;

    @ApiModelProperty(value = "接口返回业务数据", position = 2)
    private T data;
}
