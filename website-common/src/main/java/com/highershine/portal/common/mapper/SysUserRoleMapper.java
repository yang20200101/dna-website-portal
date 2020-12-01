package com.highershine.portal.common.mapper;

import com.highershine.portal.common.entity.po.SysUserRole;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description: 用户角色mapper
 * @Author: mizhanlei
 * @Date: 2019/11/26 17:14
 */
@Repository
public interface SysUserRoleMapper {

    /**
     * 根据用户id查询用户角色列表
     *
     * @param userId
     * @return
     */
    List<SysUserRole> selectSysUserRoleListByUserId(Long userId);
}
