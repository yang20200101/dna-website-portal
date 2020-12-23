package com.highershine.oauth2.server.entity;

import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description:
 * @Author: mizhanlei
 * @Date: 2020/12/1 7:31
 */
@Data
public class SysUser implements UserDetails {
    /* 用户id */
    private Long id;

    private String extId;

    /* 用户名 */
    private String username;

    private String password;

    private String nickname;

    private String province;

    private String serverNos;

    private String idCardNo;

    private Date birthDate;

    private String gender;

    private String phone;

    private String orgCode;

    private String labName;

    private String remark;

    private String city;

    private Boolean isAddOrg;

    private String area;

    private String job;

    private String orgAddCode;

    private String orgAddName;

    private String labCode;

    private Boolean status;

    private Boolean deleteFlag;

    private Date createDatetime;

    private Date updateDatetime;

    private List<SysClientRole> clientRoles = new ArrayList<>();

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
