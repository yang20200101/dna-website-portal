package com.highershine.portal.common.service;


import com.highershine.portal.common.entity.dto.AdvertisementDTO;
import com.highershine.portal.common.entity.po.Advertisement;
import com.highershine.portal.common.entity.vo.AdvertisementVo;
import com.highershine.portal.common.mapper.AdvertisementMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author zxk
 * @Date 2020/11/12 15:16
 * @Description TODO
 */
@Slf4j
@Service
public class AdvertisementServiceImpl implements AdvertisementService {
    @Resource
    private AdvertisementMapper advertisementMapper;

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
        Advertisement advertisement = advertisementMapper.selectByPrimaryKey(id);
        advertisement.setDeleted(true);
        advertisementMapper.updateByPrimaryKey(advertisement);
    }

    /**
     * 新增
     * @param advertisementDTO
     */
    @Override
    public void addAdvertisement(AdvertisementDTO advertisementDTO) throws Exception{
        Advertisement advertisement = new Advertisement();
        BeanUtils.copyProperties(advertisement, advertisementDTO);
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
        return advertisementVo;
    }

    /**
     * 编辑
     * @param advertisementDTO
     */
    @Override
    public void updateAdvertisement(AdvertisementDTO advertisementDTO) throws Exception{
        Advertisement advertisement = advertisementMapper.selectByPrimaryKey(advertisementDTO.getId());
        BeanUtils.copyProperties(advertisement, advertisementDTO);
        advertisementMapper.updateByPrimaryKey(advertisement);
    }
}
