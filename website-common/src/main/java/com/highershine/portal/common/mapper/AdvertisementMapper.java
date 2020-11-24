package com.highershine.portal.common.mapper;


import com.highershine.portal.common.entity.dto.AdvertisementDTO;
import com.highershine.portal.common.entity.po.Advertisement;
import com.highershine.portal.common.entity.vo.AdvertisementVo;

import java.util.List;

public interface AdvertisementMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Advertisement record);

    Advertisement selectByPrimaryKey(Long id);

    int updateByPrimaryKey(Advertisement record);

    List<AdvertisementVo> getAdvertisementList(AdvertisementDTO advertisementDTO);
}