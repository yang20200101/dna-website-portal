package com.highershine.portal.common.entity.po;

import lombok.Data;

import java.util.Date;

/**
 * @Description 栏目实体类
 * @Author zxk
 * @Date 2020/4/15 11:03
 **/
@Data
public class Category {

    //主键
    private Long id;

    //外部Id
    private String extId;

    //栏目名称
    private String name;

    //别名
    private String alias;

    //排序
    private Integer sort;

    //备注
    private String remark;

    //删除状态  true  删除  默认false
    private Boolean deleted;

    //状态
    private Boolean status;

    //创建时间
    private Date createdAt;

    //更新时间
    private Date updatedAt;
}
