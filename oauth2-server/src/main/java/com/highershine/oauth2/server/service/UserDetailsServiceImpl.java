package com.highershine.oauth2.server.service;

import com.highershine.oauth2.server.constants.LoginConstant;
import com.highershine.oauth2.server.entity.SysClientRole;
import com.highershine.oauth2.server.entity.SysUser;
import com.highershine.oauth2.server.exception.MyLoginException;
import com.highershine.oauth2.server.mapper.SysUserMapper;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description: userDetailsService实现类
 * @Author: mizhanlei
 * @Date: 2020/11/30 17:12
 */
@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private HttpServletRequest request;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = null;
        String type = request.getParameter("type");
        if (LoginConstant.TYPE_PASSWORD.equals(type)) {
            sysUser = sysUserMapper.selectByUsername(username);
            if (sysUser == null) {
                log.error("[{}] username not found!", username);
                throw new MyLoginException(LoginConstant.PASSWORD_EXCEPTION_PREFIX  + "用户名或密码错误");
            }
            if (StringUtils.isNotBlank(request.getParameter("password"))
                    && !BCrypt.checkpw(request.getParameter("password"), sysUser.getPassword())) {
                log.error("[{}] password error!", username);
                throw new MyLoginException(LoginConstant.PASSWORD_EXCEPTION_PREFIX  + "用户名或密码错误");
            }
            //查询系统对应角色信息
            List<SysClientRole> clientRoles = sysUserMapper.selectClientRoleByUserId(sysUser.getId());
            sysUser.setClientRoles(clientRoles);
        } else if (LoginConstant.TYPE_PKI.equals(type)) {
            String idCardNo = request.getParameter("idCardNo");
            sysUser = sysUserMapper.selectByIdCardNo(idCardNo);
            if (sysUser == null) {
                log.error("[{}] pki idCardNo not found!", idCardNo);
                throw new MyLoginException(LoginConstant.PKI_NOT_FOUND_EXCEPTION_PREFIX  + "没有查询到该用户");
            }
            List<SysClientRole> clientRoles = sysUserMapper.selectClientRoleByUserId(sysUser.getId());
            sysUser.setClientRoles(clientRoles);
            log.error("pki登录成功");
        } else {
            throw new MyLoginException("");
        }
        return sysUser;
    }
}
