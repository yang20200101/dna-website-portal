package com.highershine.portal.common.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: 系统对应角色信息
 * @Author: xueboren
 * @Date: 2020/12/21 17:14
 */
@Data
@Accessors(chain = true)
@ApiModel("系统及角色信息")
public class SysRoleListVo implements Serializable {
    @ApiModelProperty(value = "系统id", example = "zthz")
    private String clientId;

    @ApiModelProperty(value = "系统名称", example = "会战平台")
    private String clientName;

    @ApiModelProperty(value = "角色信息")
    private List<SysRoleVo> roles;
}
