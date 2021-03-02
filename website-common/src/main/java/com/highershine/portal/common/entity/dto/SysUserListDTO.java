package com.highershine.portal.common.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: 用户列表查询 dto
 * @Author: xueboren
 * @Date: 2020/12/22 14:53
 */
@Data
public class SysUserListDTO extends BaseDTO implements Serializable {
    @ApiModelProperty(value = "用户名", example = "zhangsan", required = false)
    private String username;

    @ApiModelProperty(value = "姓名", example = "张三", required = false)
    private String nickname;

    @ApiModelProperty(value = "启用", example = "true", required = false)
    private Boolean status;

    @ApiModelProperty(value = "角色id数组", example = "10086", required = false)
    private List<String> roleIds;

    @ApiModelProperty(value = "仅省级用户,默认false", example = "10086", required = false)
    private boolean provinceFlag;

    @ApiModelProperty(value = "实验室名称", example = "大兴", required = false)
    private String labName;

    //行政区划编码
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private String province;
}
