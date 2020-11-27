package com.highershine.portal.common.entity.po;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class Application {
    private Long id;

    private Long userId;

    private Long activityId;

    private Long thumbnailId;

    private Boolean isLatest;

    private Date createdAt;

    private Date updatedAt;

    private Boolean deleted;
}
