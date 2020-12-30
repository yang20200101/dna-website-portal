package com.highershine.portal.common.entity.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Description
 * @Author zxk
 * @Date 2020/4/16 15:02
 **/
@Data
@ApiModel("文章VO展示类")
public class ArticleVo {
    @ApiModelProperty(value = "主键", name = "id", example = "1", dataType = "long")
    private Long id;

    @ApiModelProperty(value = "栏目")
    private CategoryVo category;

    @ApiModelProperty(value = "发布时间", name = "publishDate", dataType = "date")
    private Date publishDate;

    @ApiModelProperty(value = "标题", name = "title", example = "外交部XXXX通报", dataType = "string")
    private String title;

    @ApiModelProperty(value = "链接", name = "link", dataType = "string")
    private String link;

    @ApiModelProperty(value = "描述", name = "description", dataType = "string")
    private String description;

    @ApiModelProperty(value = "缩略图", name = "thumbnail")
    private ThumbnailVo thumbnail;

    @ApiModelProperty(value = "文章内容", name = "content",  dataType = "string")
    private String content;

    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private Long thumbnailId;
}
