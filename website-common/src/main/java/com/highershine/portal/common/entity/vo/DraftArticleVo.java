package com.highershine.portal.common.entity.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.highershine.portal.common.entity.po.Thumbnail;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Description
 * @Author zxk
 * @Date 2020/4/20 13:03
 **/
@Data
@ApiModel("文章草稿展示实体类")
public class DraftArticleVo {

    @ApiModelProperty(value = "主键", name = "id", example = "1", dataType = "long")
    private Long id;

    @ApiModelProperty(value = "发布时间", name = "publishDate", dataType = "date")
    private Date publishDate;

    @ApiModelProperty(value = "删除状态", name = "deleted", example = "true", dataType = "bool")
    private Boolean deleted;

    @ApiModelProperty(value = "栏目表关联Id", name = "categoryId", example = "1", dataType = "long")
    private Long categoryId;

    @ApiModelProperty(value = "栏目名称", name = "categoryName", example = "今日说法", dataType = "string")
    private String categoryName;

    @ApiModelProperty(value = "是否发布", name = "isPublish", example = "true", dataType = "bool")
    private Boolean isPublish;

    @ApiModelProperty(value = "是否需要更新", name = "isNeedUpdate", example = "true", dataType = "bool")
    private Boolean isNeedUpdate;

    @ApiModelProperty(value = "是否置顶", name = "isTop", example = "true", dataType = "bool")
    private Boolean isTop;

    @ApiModelProperty(value = "标题", name = "title", example = "外交部XXXX通报", dataType = "string")
    private String title;

    @ApiModelProperty(value = "来源", name = "source", example = "XXX文章", dataType = "string")
    private String source;

    @ApiModelProperty(value = "等级", name = "level", example = "10", dataType = "int")
    private Integer level;

    @ApiModelProperty(value = "链接", name = "link", dataType = "string")
    private String link;

    @ApiModelProperty(value = "描述", name = "description", dataType = "string")
    private String description;

    @ApiModelProperty(value = "缩略图")
    private Thumbnail thumbnail;

    @ApiModelProperty(value = "是否设置为焦点图", name = "isFocus", example = "true", dataType = "bool")
    private Boolean isFocus;

    @ApiModelProperty(value = "文章内容", name = "content",  dataType = "string")
    private String content;

    @ApiModelProperty(value = "创建时间", name = "createdAt", dataType = "date")
    private Date createdAt;

    @ApiModelProperty(value = "更新时间", name = "updatedAt", dataType = "date")
    private Date updatedAt;

    @JsonIgnore
    private Long thumbnailId;

    @JsonIgnore
    private String url;
}
