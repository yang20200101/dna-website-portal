package com.highershine.portal.common.service;


import com.highershine.portal.common.entity.vo.ThumbnailVo;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Description
 * @Author zxk
 * @Date 2020/4/20 15:27
 **/
public interface ThumbnailService {
    /**
     * 上传缩略图
     * @param file
     * @param bucketName
     * @return
     */
    ThumbnailVo upload(MultipartFile file, String bucketName) throws Exception;
}
