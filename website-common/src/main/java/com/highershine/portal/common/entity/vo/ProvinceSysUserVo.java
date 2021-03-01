package com.highershine.portal.common.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description: 省级管理员信息
 * @Author: xueboren
 * @Date: 2020/12/22 14:36
 */
@Data
public class ProvinceSysUserVo implements Serializable {
    @ApiModelProperty(value = "*姓名", example = "张三", required = true)
    private String nickname;

    @ApiModelProperty(value = "*手机号码", example = "18700356478", required = true)
    private String phone;
}
