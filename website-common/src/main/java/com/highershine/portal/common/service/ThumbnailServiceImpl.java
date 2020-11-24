package com.highershine.portal.common.service;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.SetBucketPolicyRequest;
import com.highershine.portal.common.entity.po.Thumbnail;
import com.highershine.portal.common.entity.vo.ThumbnailVo;
import com.highershine.portal.common.enums.PolicyTypeEnum;
import com.highershine.portal.common.mapper.ThumbnailMapper;
import com.highershine.portal.common.utils.DateTools;
import com.highershine.portal.common.utils.PolicyUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;

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
        String keyName = name.substring(0, index) + "_" + System.currentTimeMillis() + name.substring(index);
        String suffix = name.substring(index + 1);
        String contentType = "image/" + suffix;
        ObjectMetadata objectMetadata = new ObjectMetadata();
        byte[] bytes = file.getBytes();
        int length = bytes.length;
        objectMetadata.setContentLength((long) length);
        objectMetadata.setContentType(contentType);
        boolean exist = amazonS3.doesBucketExist(bucketName);
        if (!exist) {
            amazonS3.createBucket(bucketName);
            //设置桶策略
            String policyType = PolicyUtils.getPolicyType(bucketName, PolicyTypeEnum.READ_WRITE);
            SetBucketPolicyRequest setBucketPolicyRequest =
                    new SetBucketPolicyRequest(bucketName, policyType);
            amazonS3.setBucketPolicy(setBucketPolicyRequest);
        }
        amazonS3.putObject(bucketName, keyName, file.getInputStream(), objectMetadata);

        //获取对象
        URL url = amazonS3.getUrl(bucketName, keyName);
        String path = url.getPath();
        //上传成功
        Thumbnail thumbnail = new Thumbnail();
        thumbnail.setFileName(name);
        thumbnail.setUrl(path);
        thumbnail.setBucketName(bucketName);
        thumbnail.setKeyName(keyName);
        thumbnail.setCreatedAt(DateTools.getNow());
        thumbnailMapper.insert(thumbnail);
        ThumbnailVo thumbnailVo = new ThumbnailVo();
        BeanUtils.copyProperties(thumbnailVo, thumbnail);
        return thumbnailVo;
    }

}
