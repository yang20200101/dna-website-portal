package com.highershine.portal.common.service;


import com.highershine.portal.common.entity.vo.SysRoleListVo;

import java.util.List;

/**
 * @Description 角色业务层
 * @Author xueboren
 * @Date 2020/12/23 14:38
 **/
public interface SysRoleService {
    List<SysRoleListVo> getRoleList() throws Exception;
}
