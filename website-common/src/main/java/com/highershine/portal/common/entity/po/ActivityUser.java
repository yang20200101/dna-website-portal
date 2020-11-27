package com.highershine.portal.common.entity.po;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ActivityUser {
    private Long id;

    private Long activityId;

    private Long userId;
}
