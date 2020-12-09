package com.highershine.oauth2.server.entity;

import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: TODO
 * @Author: mizhanlei
 * @Date: 2020/12/1 7:31
 */
@Data
public class SysUser implements UserDetails {
    /* 用户id */
    private Long id;

    /* 用户名 */
    private String username;

    /* 用户密码 */
    private String password;

    /* 角色列表 */
    private List<SysRole> authorities = new ArrayList<>();

    /* 指示是否未过期的用户的凭据(密码),过期的凭据防止认证 默认true 默认表示未过期 */
    private boolean credentialsNonExpired = true;

    //账户是否未过期,过期无法验证 默认true表示未过期
    private boolean accountNonExpired = true;

    //用户是未被锁定,锁定的用户无法进行身份验证 默认true表示未锁定
    private boolean accountNonLocked = true;

    //是否可用 ,禁用的用户不能身份验证 默认true表示可用
    private boolean enabled = true;



}
