package com.highershine.portal.common.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Description: 角色信息
 * @Author: xueboren
 * @Date: 2020/12/23 13:14
 */
@Data
@Accessors(chain = true)
@ApiModel("角色信息")
public class SysRoleVo implements Serializable {
    @ApiModelProperty(value = "角色id", example = "zthz")
    private String roleId;

    @ApiModelProperty(value = "角色名称", example = "会战平台")
    private String roleName;

    @ApiModelProperty(value = "1默认角色;0不是默认", example = "1")
    private String defaultFlag;
}
