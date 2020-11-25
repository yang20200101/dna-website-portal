package com.highershine.portal.common.entity.po;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class DraftArticle {
    private Long id;

    private String extId;

    private Date publishDate;

    private Boolean deleted;

    private String categoryExtId;

    private Long categoryId;

    private Boolean isPublish;

    private Boolean isNeedUpdate;

    private Boolean isTop;

    private String title;

    private String source;

    private String link;

    private String description;

    private Long thumbnailId;

    private Boolean isFocus;

    private Integer level;

    private String content;

    private Date createdAt;

    private Date updatedAt;

}
