package com.highershine.portal.common.service;

import com.highershine.portal.common.entity.bo.SysUserBo;
import com.highershine.portal.common.entity.dto.UpdatePasswordDTO;


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

    /**
     * 查询oauth回调地址
     * @return
     */
    String selectOauthRedirectUri(String clientId);

    /**
     * 重置密码
     * @param id
     */
    void resetPassword(Long id);

    /**
     * 校验原始密码
     * @param dto
     * @return
     */
    boolean validSrcPassword(UpdatePasswordDTO dto);

    /**
     * 修改密码
     * @param dto
     */
    void updatePassword(UpdatePasswordDTO dto);
}
