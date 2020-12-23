package com.highershine.portal.common.entity.vo;

import com.highershine.portal.common.entity.bo.ClientRoleBo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Description: 用户实体类
 * @Author: xueboren
 * @Date: 2020/12/22 14:36
 */
@Data
public class FindSysUserVo implements Serializable {
    @ApiModelProperty(value = "用户id（修改必填）", example = "10086", required = false)
    private Long id;

    @ApiModelProperty(value = "*用户名", example = "zhangsan", required = true)
    private String username;

    @ApiModelProperty(value = "*姓名", example = "张三", required = true)
    private String nickname;

    @ApiModelProperty(value = "*省份编码", example = "130000", required = true)
    private String province;

    @ApiModelProperty(value = "地市编码", example = "130100", required = true)
    private String city;

    @ApiModelProperty(value = "区县编码", example = "130102", required = true)
    private String area;

    @ApiModelProperty(value = "*身份证号", example = "23018319901231234156", required = true)
    private String idCardNo;

    @ApiModelProperty(value = "*出生日期", example = "2000-01-05", required = true)
    private Date birthDate;

    @ApiModelProperty(value = "*性别", example = "1", required = true)
    private String gender;

    @ApiModelProperty(value = "*手机号码", example = "18700356478", required = true)
    private String phone;

    @ApiModelProperty(value = "*工作岗位", example = "1", required = true)
    private String job;

    @ApiModelProperty(value = "是否新增单位,默认false", example = "false", required = false)
    private Boolean isAddOrg;

    @ApiModelProperty(value = "单位编码", example = "13000000000", required = false)
    private String orgCode;

    @ApiModelProperty(value = "单位名称", example = "河北省公安厅", required = false)
    private String labName;

    @ApiModelProperty(value = "管辖地区数组", example = "【130000,130010】", required = false)
    private List<String> serverNoList;

    @ApiModelProperty(value = "备注", example = "备注", required = false)
    private String remark;

    @ApiModelProperty(value = "启用，默认true", example = "true", required = false)
    private Boolean status;

    @ApiModelProperty(value = "系统对应角色信息")
    private List<ClientRoleBo> clientRoles;
}
