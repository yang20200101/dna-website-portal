package com.highershine.portal.common.entity.po;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class Activity {
    private Long id;

    private String title;

    private Date date;

    private String description;

    private Long thumbnailId;

    private Integer applyNumber;

    private String content;

    private Boolean deleted;

    private Date createdAt;

    private Date updatedAt;

}
