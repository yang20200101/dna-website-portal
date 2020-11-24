package com.highershine.portal.common.entity.po;

import lombok.Data;

import java.util.Date;

@Data
public class Advertisement {
    private Long id;

    private String extId;

    private String title;

    private String link;

    private String thumbnailId;

    private String position;

    private String level;

    private Boolean isPublish;

    private Date createdAt;

    private Date updatedAt;

    private Boolean deleted;

}