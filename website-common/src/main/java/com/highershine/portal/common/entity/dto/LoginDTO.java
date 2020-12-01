package com.highershine.portal.common.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @Description: 用户DTO对象
 * @Author: mizhanlei
 * @Date: 2019/11/27 14:47
 */
@Data
@ApiModel(value = "系统用户", description = "系统用户对象")
public class LoginDTO implements Serializable {
    // 用户名
    @ApiModelProperty(
            value = "*用户名",
            name = "username",
            example = "zhangsan",
            required = true)
    @NotBlank(message = "用户名为空")
    private String username;

    // 密码
    @ApiModelProperty(
            value = "密码",
            name = "password",
            example = "fd2b8862e06645c3a2dfe542fe4cd956",
            required = true)
    @NotBlank(message = "密码为空")
    private String password;

}
