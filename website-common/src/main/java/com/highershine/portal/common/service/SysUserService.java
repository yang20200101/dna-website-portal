package com.highershine.portal.common.service;

import com.highershine.portal.common.entity.bo.SysUserBo;


/**
 * @Description: 用户服务层接口
 * @Author: mizhanlei
 * @Date: 2019/11/26 17:56
 */
public interface SysUserService {

    /**
     * 根据用户名查询
     *
     * @param username
     */
    SysUserBo selectByUsername(String username);
}
