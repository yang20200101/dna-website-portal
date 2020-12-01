package com.highershine.portal.common.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description: 用户登录展示对象
 * @Author: mizhanlei
 * @Date: 2019/12/18 15:57
 */
@Data
@ApiModel("用户登录展示对象")
public class SysUserLoginVo implements Serializable {
    @ApiModelProperty(value = "是否登录成功", example = "true")
    private Boolean result;

    @ApiModelProperty(value = "jsessionid", example = "6b86b273ff34fce19d6b804eff5a3f5747ada4eaa22f1d49c01e52ddb7875b4b")
    private String jsessionid;

    @ApiModelProperty(value = "用户id", example = "76", dataType = "long")
    private Long id;
}
