package com.highershine.portal.common.entity.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @Description: 系统对应角色信息
 * @Author: xueboren
 * @Date: 2020/12/21 17:14
 */
@Data
@ApiModel("系统对应角色信息")
public class ClientRoleBo implements Serializable {
    @ApiModelProperty(value = "*角色id数组", example = "10086")
    @NotNull(message = "角色为空")
    private List<String> roles;


    @ApiModelProperty(value = "*系统id", example = "zthz")
    @NotBlank(message = "系统id为空")
    private String clientId;
}
