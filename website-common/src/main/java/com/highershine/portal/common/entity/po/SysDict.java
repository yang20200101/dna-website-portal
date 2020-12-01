package com.highershine.portal.common.entity.po;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 字典
 * @Author: xueboren
 * @Date: 2019/11/27 19:20
 */
@Data
public class SysDict implements Serializable {
    //主键ID
    private Long id;

    //字典类型
    private String dictCategory;

    //字典键
    private String dictKey;

    //字典键
    private String dictNationalKey;

    //字典值
    private String dictValue1;

    //字典值2
    private String dictValue2;

    //字典值3
    private String dictValue3;

    //排序字段
    private Long ord;

    //是否激活
    private Boolean activeFlag;

    //预留字段
    private String remark;

    //创建人
    private String createUser;

    //创建时间
    private Date createDatetime;

    //更新用户
    private String updateUser;

    //更新时间
    private Date updateDatetime;

    //删除标识
    private Boolean deleteFlag;
}
