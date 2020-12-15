package com.highershine.portal.common.service;


import com.highershine.portal.common.entity.dto.AdvertisementDTO;
import com.highershine.portal.common.entity.po.Advertisement;
import com.highershine.portal.common.entity.po.Thumbnail;
import com.highershine.portal.common.entity.vo.AdvertisementVo;
import com.highershine.portal.common.mapper.AdvertisementMapper;
import com.highershine.portal.common.mapper.ThumbnailMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @Author zxk
 * @Date 2020/11/12 15:16
 * @Description
 */
@Slf4j
@Service
public class AdvertisementServiceImpl implements AdvertisementService {
    @Resource
    private AdvertisementMapper advertisementMapper;
    @Resource
    private ThumbnailMapper thumbnailMapper;

    /**
     * 获取飘窗列表
     *
     * @param advertisementDTO
     * @return
     */
    @Override
    public List<AdvertisementVo> getAdvertisementList(AdvertisementDTO advertisementDTO) {
        List<AdvertisementVo> advertisementVos = advertisementMapper.getAdvertisementList(advertisementDTO);
        return advertisementVos;
    }

    /**
     * 删除
     * @param id
     */
    @Override
    public void deleteAdvertisement(long id) {
        advertisementMapper.deleteFlagByPrimaryKey(id);
    }

    /**
     * 新增
     * @param advertisementDTO
     */
    @Override
    public void addAdvertisement(AdvertisementDTO advertisementDTO) throws Exception{
        Advertisement advertisement = new Advertisement();
        BeanUtils.copyProperties(advertisementDTO, advertisement);
        advertisement.setDeleted(false).setCreatedAt(new Date()).setUpdatedAt(new Date())
                .setThumbnailId(advertisementDTO.getThumbnail().getId());
        advertisementMapper.insert(advertisement);
    }

    /**
     * 查询
     * @param id
     * @return
     */
    @Override
    public AdvertisementVo findAdvertisementById(long id) throws Exception{
        Advertisement advertisement = advertisementMapper.selectByPrimaryKey(id);
        AdvertisementVo advertisementVo = new AdvertisementVo();
        BeanUtils.copyProperties(advertisement, advertisementVo);
        Thumbnail thumbnail = thumbnailMapper.selectByPrimaryKey(advertisement.getThumbnailId());
        advertisementVo.setThumbnail(thumbnail);
        return advertisementVo;
    }

    /**
     * 编辑
     * @param advertisementDTO
     */
    @Override
    public void updateAdvertisement(AdvertisementDTO advertisementDTO) throws Exception{
        Advertisement advertisement = advertisementMapper.selectByPrimaryKey(advertisementDTO.getId());
        BeanUtils.copyProperties(advertisementDTO, advertisement);
        advertisement.setThumbnailId(advertisementDTO.getThumbnail().getId()).setUpdatedAt(new Date());
        advertisementMapper.updateByPrimaryKey(advertisement);
    }
}
