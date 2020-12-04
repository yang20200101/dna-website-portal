package com.highershine.oauth2.server.service;

import com.highershine.oauth2.server.entity.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @Description: userDetailsService实现类
 * @Author: mizhanlei
 * @Date: 2020/11/30 17:12
 */
@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (!"admin".equals(username)) {
            throw new UsernameNotFoundException(username + " is not exists!");
        }

        String password = passwordEncoder.encode("123456");
//        String password = "$2b$10$JPrw5a13QDfMHtmoTPzGoOG586HxExfXmRQ/fpf7oQH3UzSejpX36";
        log.info("【登录】MM：{}", password);
        log.info("【登录】MM：{}", passwordEncoder.encode("2020"));
        return new SysUser("admin", password,
                AuthorityUtils.commaSeparatedStringToAuthorityList("admin,ROLE_admin, /index.html"));
    }
}
