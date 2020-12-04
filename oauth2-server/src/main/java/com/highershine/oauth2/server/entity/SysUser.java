package com.highershine.oauth2.server.entity;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

/**
 * @Description: TODO
 * @Author: mizhanlei
 * @Date: 2020/12/1 7:31
 */
@Data
public class SysUser implements UserDetails {

    private String username;

    private String password;

    private List<GrantedAuthority> authorities;

    public SysUser(String username, String password, List<GrantedAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
