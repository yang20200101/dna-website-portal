package com.highershine.portal.common.entity.po;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Description: 系统权限表
 * @Author: mizhanlei
 * @Date: 2019/11/26 15:23
 */
@Data
@Accessors(chain = true)
public class SysUserRole implements Serializable {
    // 主键id
    private Long id;

    // 用户id
    private Long userId;

    // 角色id
    private String roleId;

    // 客户端id
    private String clientId;
}
