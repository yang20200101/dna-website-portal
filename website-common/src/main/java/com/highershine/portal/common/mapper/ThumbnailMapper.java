package com.highershine.portal.common.mapper;


import com.highershine.portal.common.entity.po.Thumbnail;
import org.springframework.stereotype.Repository;

@Repository
public interface ThumbnailMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Thumbnail record);

    Thumbnail selectByPrimaryKey(Long id);

    int updateByPrimaryKey(Thumbnail record);
}
