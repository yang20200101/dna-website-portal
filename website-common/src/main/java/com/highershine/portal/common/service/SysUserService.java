package com.highershine.portal.common.service;

import com.github.pagehelper.PageInfo;
import com.highershine.portal.common.entity.dto.SysUserDTO;
import com.highershine.portal.common.entity.dto.SysUserListDTO;
import com.highershine.portal.common.entity.dto.UpdatePasswordDTO;
import com.highershine.portal.common.entity.po.SysUser;
import com.highershine.portal.common.entity.vo.FindSysUserVo;
import com.highershine.portal.common.entity.vo.SysUserListVo;


/**
 * @Description: 用户服务层接口
 * @Author: mizhanlei
 * @Date: 2019/11/26 17:56
 */
public interface SysUserService {


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

    /**
     * 用户注册
     * @param dto
     * @return
     */
    SysUser register(SysUserDTO dto) throws Exception;

    /**
     * 用户校验和注册
     * @param dto
     * @return
     */
    String registerAndValid(SysUserDTO dto) throws Exception;

    /**
     * 根据id查询用户信息
     * @param id
     * @return
     */
    FindSysUserVo findUserById(Long id);

    /**
     * 查询用户列表
     * @param dto
     * @return
     */
    PageInfo<SysUserListVo> getUserList(SysUserListDTO dto);

    /**
     * 删除用户
     * @param id
     * @return
     */
    void deleteUserById(Long id);
}
