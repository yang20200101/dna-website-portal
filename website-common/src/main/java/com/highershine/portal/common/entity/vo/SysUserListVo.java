package com.highershine.portal.common.entity.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Description: 用户实体类
 * @Author: xueboren
 * @Date: 2020/12/22 14:36
 */
@Data
@Accessors(chain = true)
public class SysUserListVo implements Serializable {
    @ApiModelProperty(value = "用户id（修改必填）", example = "10086")
    private Long id;

    @ApiModelProperty(value = "*用户名", example = "zhangsan")
    private String username;

    @ApiModelProperty(value = "*姓名", example = "张三")
    private String nickname;

    @ApiModelProperty(value = "*所在地区", example = "河北省唐山市")
    private String address;

    @ApiModelProperty(value = "*手机号码", example = "18700356478")
    @NotBlank(message = "手机号码为空")
    private String phone;

    @ApiModelProperty(value = "单位名称", example = "河北省公安厅")
    private String labName;

    @ApiModelProperty(value = "启用", example = "true")
    private Boolean status;

    @ApiModelProperty(value = "角色", example = "[管理员,普通用户]")
    private List<String> roleNameList;

    @ApiModelProperty(value = "更新时间", example = "2020-12-08 14:04")
    private String updateDateStr;

    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private String province;

    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private String city;

    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private String area;

    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private Date updateDatetime;
}
