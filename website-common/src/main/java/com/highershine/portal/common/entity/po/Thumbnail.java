package com.highershine.portal.common.entity.po;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class Thumbnail {
    private Long id;

    private String bucketName;

    private String keyName;

    private String fileName;

    private String url;

    private Boolean deleted;

    private Date createdAt;

    private Date updatedAt;

}
