package com.highershine.portal.common.entity.po;

import lombok.Data;

import java.util.Date;

/**
 * @Description: 行政区划
 * @Author: xueboren
 * @Date: 2019/11/27 19:20
 */
@Data
public class SysRegionalism {
    //主键ID
    private Long id;

    //区划值
    private String regionalismCode;

    //区划名称
    private String regionalismName;

    //父节点区划值
    private String parentCode;

    //拼音简写
    private String spellShort;

    //是否激活
    private Boolean activeFlag;

    //删除标识
    private Boolean deleteFlag;

    //备注
    private String remark;

    //创建人
    private String createUser;

    //创建时间
    private Date createDatetime;

    //更新用户
    private String updateUser;

    //更新时间
    private Date updateDatetime;
}
