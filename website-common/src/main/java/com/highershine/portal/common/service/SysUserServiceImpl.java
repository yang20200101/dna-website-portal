package com.highershine.portal.common.service;

import com.highershine.portal.common.entity.bo.SysUserBo;
import com.highershine.portal.common.entity.dto.UpdatePasswordDTO;
import com.highershine.portal.common.entity.po.SysUser;
import com.highershine.portal.common.entity.po.SysUserRole;
import com.highershine.portal.common.mapper.SysUserMapper;
import com.highershine.portal.common.mapper.SysUserRoleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description: 用户服务层
 * @Author: mizhanlei
 * @Date: 2019/11/26 17:57
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class SysUserServiceImpl implements SysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;


    /**
     * 根据用户名查询
     *
     * @param username
     */
    @Override
    public SysUserBo selectByUsername(String username) {
        SysUserBo sysUserBo = null;
        // 查询用户信息
        SysUser sysUser = this.sysUserMapper.selectByUsername(username);
        // 查询用户角色列表
        if (sysUser != null) {
            sysUserBo = new SysUserBo();
            List<SysUserRole> sysUserRoleList = this.sysUserRoleMapper.selectSysUserRoleListByUserId(sysUser.getId());
            sysUserBo.setSysUser(sysUser);
            sysUserBo.setSysUserRoleList(sysUserRoleList);
        }
        return sysUserBo;
    }

    /**
     * 查询oauth回调地址
     * @return
     */
    @Override
    public String selectOauthRedirectUri(String clientId) {
        return this.sysUserMapper.selectOauthRedirectUri(clientId);
    }

    /**
     * 重置密码
     * @param id
     */
    @Override
    public void resetPassword(Long id) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        //encodeResult 为需要存入数据中加盐加密后的密码
        String encodeResult = bCryptPasswordEncoder.encode("111111");
        this.sysUserMapper.updatePassword(id, encodeResult);
    }

    /**
     * 校验原始密码
     * @param dto
     * @return
     */
    @Override
    public boolean validSrcPassword(UpdatePasswordDTO dto) {
        SysUser sysUser = sysUserMapper.selectByPrimaryKey(dto.getId());
        if (BCrypt.checkpw(dto.getSrcPassword(), sysUser.getPassword())) {
            //原密码正确
            return true;
        }
        return false;
    }

    /**
     * 修改密码
     * @param dto
     */
    @Override
    public void updatePassword(UpdatePasswordDTO dto) {
        sysUserMapper.updatePassword(dto.getId(), dto.getPassword());
    }
}
