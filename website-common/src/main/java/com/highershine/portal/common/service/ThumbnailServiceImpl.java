package com.highershine.portal.common.service;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.highershine.portal.common.entity.po.Thumbnail;
import com.highershine.portal.common.entity.vo.ThumbnailVo;
import com.highershine.portal.common.mapper.ThumbnailMapper;
import com.highershine.portal.common.utils.DateTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

/**
 * @Description
 * @Author zxk
 * @Date 2020/4/20 15:28
 **/
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class ThumbnailServiceImpl implements ThumbnailService {
    @Autowired
    private AmazonS3 amazonS3;
    @Autowired
    private ThumbnailMapper thumbnailMapper;

    /**
     * 上传
     * @param file
     * @param bucketName
     * @return
     */
    @Override
    public ThumbnailVo upload(MultipartFile file, String bucketName) throws Exception {
        String name = file.getOriginalFilename();
        int index = name.indexOf(".");
        String yyyyMM = DateTools.dateToString(new Date(), "yyyyMM");
        String keyName = yyyyMM + "/" + name.substring(0, index) + "_"
                + DateTools.dateToString(new Date(), DateTools.DF_COMPACT_TIME) + name.substring(index);
        String suffix = name.substring(index + 1);
        String contentType = "image/" + suffix;
        ObjectMetadata objectMetadata = new ObjectMetadata();
        byte[] bytes = file.getBytes();
        int length = bytes.length;
        objectMetadata.setContentLength((long) length);
        objectMetadata.setContentType(contentType);
        amazonS3.putObject(bucketName, keyName, file.getInputStream(), objectMetadata);
        //上传成功
        Thumbnail thumbnail = new Thumbnail();
        thumbnail.setFileName(name);
        thumbnail.setUrl(keyName);
        thumbnail.setBucketName(bucketName);
        thumbnail.setCreatedAt(DateTools.getNow());
        thumbnail.setUpdatedAt(DateTools.getNow());
        thumbnail.setDeleted(false);
        thumbnailMapper.insert(thumbnail);
        ThumbnailVo thumbnailVo = new ThumbnailVo();
        BeanUtils.copyProperties(thumbnail, thumbnailVo);
        return thumbnailVo;
    }

}
