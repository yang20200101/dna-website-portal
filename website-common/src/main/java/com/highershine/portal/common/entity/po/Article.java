package com.highershine.portal.common.entity.po;

import lombok.Data;

import java.util.Date;

@Data
public class Article {
    private Long id;

    private String extId;

    private Date publishDate;

    private Boolean deleted;

    private Integer level;

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

    private String content;

    private String draftExtId;

    private Long draftId;

    private Date createdAt;

    private Date updatedAt;

}
