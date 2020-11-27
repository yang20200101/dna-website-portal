package com.highershine.portal.common.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: 行政区划
 * @Author: xueboren
 * @Date: 2019/11/27 19:20
 */
@Data
@ApiModel(value = "行政区划展示对象")
public class SysRegionalismVo implements Serializable {
    //主键ID
    @ApiModelProperty(value = "主键ID", example = "1", dataType = "long")
    private Long id;

    //区划值
    @ApiModelProperty(value = "行政区划编码", example = "130102")
    private String regionalismCode;

    //区划名称
    @ApiModelProperty(value = "行政区划名称", example = "河北省石家庄市裕华区")
    private String regionalismName;

    //父节点区划值
    @ApiModelProperty(value = "父行政区划编码", example = "130100")
    private String parentCode;

    // 子节点
    @ApiModelProperty(value = "下属行政区划列表")
    private List<SysRegionalismVo> children;

    public int compareTo(SysRegionalismVo sysRegionalismVo) {
        return regionalismCode.compareTo(sysRegionalismVo.getRegionalismCode());
    }
}
