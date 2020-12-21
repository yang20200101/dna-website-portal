package com.highershine.portal.common.entity.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @Description: 用户Bo实体类
 * @Author: mizhanlei
 * @Date: 2019/11/27 15:14
 */
@Data
@ApiModel("活动报名信息Vo类")
public class ClientRoleBo implements Serializable {
    @ApiModelProperty(value = "*角色id", example = "10086")
    @NotBlank(message = "角色id为空")
    private String roleId;

    @ApiModelProperty(value = "角色名称", example = "管理员")
    private String roleName;

    @ApiModelProperty(value = "*系统id", example = "zthz")
    @NotBlank(message = "系统id为空")
    private String clientId;

    @ApiModelProperty(value = "系统名称", example = "会战平台")
    private String clientName;
}
