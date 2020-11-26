package com.highershine.portal.common.mapper;


import com.highershine.portal.common.entity.dto.AdvertisementDTO;
import com.highershine.portal.common.entity.po.Advertisement;
import com.highershine.portal.common.entity.vo.AdvertisementVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdvertisementMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Advertisement record);

    Advertisement selectByPrimaryKey(Long id);

    int updateByPrimaryKey(Advertisement record);

    /**
     * 假删除
     * @param id
     * @return
     */
    int deleteFlagByPrimaryKey(Long id);

    List<AdvertisementVo> getAdvertisementList(AdvertisementDTO advertisementDTO);
}