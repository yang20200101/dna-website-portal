package com.highershine.oauth2.server.service;

import com.highershine.oauth2.server.entity.SysClientRole;
import com.highershine.oauth2.server.entity.SysUser;
import com.highershine.oauth2.server.exception.MyUsernameNotFoundException;
import com.highershine.oauth2.server.mapper.SysUserMapper;
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
        SysUser sysUser = sysUserMapper.selectByUsername(username);
        if (sysUser == null) {
            throw new MyUsernameNotFoundException(username + " is not exists!");
        }
        if (!BCrypt.checkpw(request.getParameter("password"), sysUser.getPassword())) {
            throw new MyUsernameNotFoundException(username + " password error!");
        }
        //查询系统对应角色信息
        List<SysClientRole> clientRoles = sysUserMapper.selectClientRoleByUserId(sysUser.getId());
        sysUser.setClientRoles(clientRoles);
        return sysUser;
    }
}
