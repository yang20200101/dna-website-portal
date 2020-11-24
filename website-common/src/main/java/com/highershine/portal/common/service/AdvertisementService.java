package com.highershine.portal.common.service;


import com.highershine.portal.common.entity.dto.AdvertisementDTO;
import com.highershine.portal.common.entity.vo.AdvertisementVo;

import java.util.List;

/**
 * 飘窗业务层
 */
public interface AdvertisementService {
    /**
     * 获取飘窗管理列表
     * @param advertisementDTO
     * @return
     */
    List<AdvertisementVo> getAdvertisementList(AdvertisementDTO advertisementDTO);

    /**
     * 删除
     * @param id
     */
    void deleteAdvertisement(long id);

    /**
     * 新增
     * @param advertisementDTO
     */
    void addAdvertisement(AdvertisementDTO advertisementDTO) throws Exception;

    /**
     * 根据ID查询详细信息
     * @param id
     * @return
     */
    AdvertisementVo findAdvertisementById(long id) throws Exception;

    /**
     * 编辑
     * @param advertisementDTO
     */
    void updateAdvertisement(AdvertisementDTO advertisementDTO) throws Exception;
}
