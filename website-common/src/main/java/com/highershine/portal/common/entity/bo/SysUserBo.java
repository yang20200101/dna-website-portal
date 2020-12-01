package com.highershine.portal.common.entity.bo;

import com.highershine.portal.common.entity.po.SysUser;
import com.highershine.portal.common.entity.po.SysUserRole;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: 用户Bo实体类
 * @Author: mizhanlei
 * @Date: 2019/11/27 15:14
 */
@Data
public class SysUserBo implements Serializable {

    // 用户信息
    private SysUser sysUser;

    // 用户角色信息
    private List<SysUserRole> sysUserRoleList;
}
