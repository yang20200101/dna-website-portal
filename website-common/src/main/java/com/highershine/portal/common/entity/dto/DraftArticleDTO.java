package com.highershine.portal.common.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Description 文章草稿DTO对象
 * @Author zxk
 * @Date 2020/4/20 10:13
 **/
@Data
public class DraftArticleDTO extends BaseDTO {

    @ApiModelProperty(value = "主键", name = "id", example = "1", dataType = "long")
    private Long id;

    @ApiModelProperty(value = "外部关联ID", name = "extId", example = "6fQDyTuEvH", dataType = "string")
    private String extId;

    @ApiModelProperty(value = "发布时间", name = "publishDate", dataType = "date")
    @NotNull(message = "发布日期为空")
    private Date publishDate;

    @ApiModelProperty(value = "删除状态", name = "deleted", example = "true", dataType = "bool")
    private Boolean deleted;

    @ApiModelProperty(value = "原栏目表关联Id", name = "categoryExtId", example = "6fQDyTuEvH", dataType = "string")
    private String categoryExtId;

    @ApiModelProperty(value = "栏目表关联Id", name = "categoryId", example = "1", dataType = "long")
    @NotNull(message = "所属栏目为空")
    private Long categoryId;

    @ApiModelProperty(value = "是否发布", name = "isPublish", example = "true", dataType = "bool")
    private Boolean isPublish;

    @ApiModelProperty(value = "是否需要更新", name = "isNeedUpdate", example = "true", dataType = "bool")
    private Boolean isNeedUpdate;

    @ApiModelProperty(value = "是否置顶", name = "isTop", example = "true", dataType = "bool")
    private Boolean isTop;

    @ApiModelProperty(value = "标题", name = "title", example = "外交部XXXX通报", dataType = "string")
    @NotNull(message = "标题为空")
    private String title;

    @ApiModelProperty(value = "来源", name = "source", example = "XXX文章", dataType = "string")
    private String source;

    @ApiModelProperty(value = "链接", name = "link", dataType = "string")
    private String link;

    @ApiModelProperty(value = "描述", name = "description", dataType = "string")
    private String description;

    @ApiModelProperty(value = "缩略图地址", name = "thumbnailId", dataType = "long")
    private Long thumbnailId;

    @ApiModelProperty(value = "是否设置为焦点图", name = "isFocus", example = "true", dataType = "bool")
    private Boolean isFocus;

    @ApiModelProperty(value = "文章内容", name = "content", dataType = "string")
    private String content;

    @ApiModelProperty(value = "创建时间", name = "createdAt", dataType = "date")
    private Date createdAt;

    @ApiModelProperty(value = "更新时间", name = "updatedAt", dataType = "date")
    private Date updatedAt;

    //添加栏目别名查询字段
    @ApiModelProperty(value = "文章栏目别名", name = "categoryAlias", example = "GZDT", dataType = "string")
    private String categoryAlias;

    @ApiModelProperty(value = "关键字查询", name = "keyword", example = "天不错", dataType = "string")
    private String keyword;
}
