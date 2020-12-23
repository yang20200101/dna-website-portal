package com.highershine.portal.common.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.highershine.portal.common.entity.bo.ClientRoleBo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Description: 用户实体类
 * @Author: mizhanlei
 * @Date: 2019/11/26 8:46
 */
@Data
public class SysUserDTO implements Serializable {
    @ApiModelProperty(value = "用户id（修改必填）", example = "10086", required = false)
    private Long id;

    @ApiModelProperty(value = "*用户名", example = "zhangsan", required = true)
    @NotBlank(message = "用户名为空")
    private String username;

    @ApiModelProperty(value = "*姓名", example = "张三", required = true)
    @NotBlank(message = "姓名为空")
    private String nickname;

    @ApiModelProperty(value = "*密码", example = "123456", required = true)
    @NotBlank(message = "密码为空")
    private String password;

    @ApiModelProperty(value = "*省份编码", example = "130000", required = true)
    @NotBlank(message = "省份为空")
    private String province;

    @ApiModelProperty(value = "地市编码", example = "130100", required = true)
    private String city;

    @ApiModelProperty(value = "区县编码", example = "130102", required = true)
    private String area;

    @ApiModelProperty(value = "*身份证号", example = "23018319901231234156", required = true)
    @NotBlank(message = "身份证号为空")
    private String idCardNo;

    @ApiModelProperty(value = "*出生日期", example = "2000-01-05", required = true)
    @NotNull(message = "出生日期为空")
    private Date birthDate;

    @ApiModelProperty(value = "*性别", example = "1", required = true)
    @NotBlank(message = "性别为空")
    private String gender;

    @ApiModelProperty(value = "*手机号码", example = "18700356478", required = true)
    @NotBlank(message = "手机号码为空")
    private String phone;

    @ApiModelProperty(value = "*工作岗位", example = "1", required = true)
    @NotBlank(message = "工作岗位为空")
    private String job;

    @ApiModelProperty(value = "是否新增单位,默认false", example = "false", required = false)
    private Boolean isAddOrg;

    @ApiModelProperty(value = "单位编码", example = "13000000000", required = false)
    private String orgCode;

    @ApiModelProperty(value = "单位名称", example = "河北省公安厅", required = false)
    private String labName;

    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private String serverNos;

    //已废弃
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private String orgAddCode;

    //已废弃
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private String orgAddName;

    //已废弃
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private String labCode;

    @ApiModelProperty(value = "备注", example = "备注", required = false)
    private String remark;

    @ApiModelProperty(value = "启用，默认true", example = "true", required = false)
    private Boolean status;

    @ApiModelProperty(value = "系统对应角色信息")
    private List<ClientRoleBo> clientRoles;
}
