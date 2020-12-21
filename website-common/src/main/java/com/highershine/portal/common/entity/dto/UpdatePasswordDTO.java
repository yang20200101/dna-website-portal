package com.highershine.portal.common.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class UpdatePasswordDTO implements Serializable {
    @ApiModelProperty(value = "用户id", example = "10086")
    @NotNull(message = "用户id为空")
    private Long id;

    @ApiModelProperty(value = "原始密码", example = "111111")
    @NotBlank(message = "原始密码为空")
    private String srcPassword;

    @ApiModelProperty(value = "要修改的密码", example = "123456")
    @NotBlank(message = "要修改的密码为空")
    private String password;
}
