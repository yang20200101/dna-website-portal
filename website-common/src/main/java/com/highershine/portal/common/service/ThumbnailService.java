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

    /**
     * 下载活动报名信息接口
     * @param bucketName
     * @param activityId
     */
    void downloadActivityInfo(String bucketName, Long activityId) throws Exception;

    /**
     * 附件下载
     * @param bucketName
     * @param id
     */
    void download(String bucketName, Long id) throws Exception;
}
