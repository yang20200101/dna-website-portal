package com.highershine.portal.common.entity.po;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description: 系统权限表
 * @Author: mizhanlei
 * @Date: 2019/11/26 15:23
 */
@Data
public class SysUserRole implements Serializable {

    // 主键id
    private Long id;

    // 用户id
    private Long userId;

    // 角色id
    private String roleId;
}
