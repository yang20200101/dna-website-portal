package com.highershine.portal.common.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.highershine.portal.common.entity.bo.ClientRoleBo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Description: 用户实体类
 * @Author: mizhanlei
 * @Date: 2019/11/26 8:46
 */
@Data
public class SysUserVo implements Serializable {
    private Long id;

    private String extId;

    private String username;

    private String nickname;

    private String password;

    private String province;

    private String serverNos;

    private List<String> serverNoList;

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

    private Boolean status;

    private Boolean deleteFlag;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDatetime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateDatetime;

    @ApiModelProperty(value = "用户角色列表")
    private List<String> userRole;

    @ApiModelProperty(value = "系统对应角色信息")
    private List<ClientRoleBo> clientRoles;

    @ApiModelProperty(value = "用户菜单列表")
    private String[] userMenu;
}
