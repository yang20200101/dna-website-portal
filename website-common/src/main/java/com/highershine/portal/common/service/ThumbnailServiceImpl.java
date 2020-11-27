package com.highershine.portal.common.service;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.highershine.portal.common.constants.CommonConstant;
import com.highershine.portal.common.entity.po.Activity;
import com.highershine.portal.common.entity.po.Application;
import com.highershine.portal.common.entity.po.Thumbnail;
import com.highershine.portal.common.entity.vo.ActivityUserVo;
import com.highershine.portal.common.entity.vo.ThumbnailVo;
import com.highershine.portal.common.mapper.ActivityMapper;
import com.highershine.portal.common.mapper.ThumbnailMapper;
import com.highershine.portal.common.utils.DateTools;
import com.highershine.portal.common.utils.FileUtil;
import com.highershine.portal.common.utils.StringUtil;
import com.highershine.portal.common.utils.ZIPUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.util.Date;
import java.util.List;

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
    private HttpServletResponse response;
    @Autowired
    private ThumbnailMapper thumbnailMapper;
    @Autowired
    private ActivityMapper activityMapper;
    @Autowired
    private SysRegionalismService regionalismService;

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


    @Override
    public void downloadActivityInfo(String bucketName, Long activityId) throws Exception {
        //查询需要的信息
        Activity activity = activityMapper.selectByPrimaryKey(activityId);
        Application application = new Application();
        application.setActivityId(activityId).setIsLatest(true);
        List<ActivityUserVo> list = activityMapper.getActivityUserList(application);
        //压缩路径 压缩包名
        String zipName = StringUtil.dealFileName(activity.getTitle())
                + DateTools.dateToString(activity.getDate());
        String zipPath = CommonConstant.TMP_PATH + "/"  + zipName;
        //生成临时文件
        for (ActivityUserVo activityUserVo : list) {
            getMaterialFile(zipPath, activityUserVo.getNickname() +
                            "(" + regionalismService.getNameByCode(activityUserVo.getProvince())+ ")",
                    bucketName, activityUserVo.getThumbnailId());
        }
        //生成压缩包 并下载
        zipDownload(zipPath, CommonConstant.TMP_PATH, zipName + ".zip");
    }

    @Override
    public void download(String bucketName, Long id) throws Exception {
        Thumbnail thumbnail = thumbnailMapper.selectByPrimaryKey(id);
        minIODownload(bucketName, thumbnail.getUrl(), thumbnail.getFileName());
    }

    /**
     * 生成文件到本地
     * @param path 目录
     * @param fileName 文件名
     * @param bucketName
     * @param thumbnailId
     * @throws Exception
     */
    private void getMaterialFile(String path, String fileName, String bucketName, Long thumbnailId) throws Exception {
        Thumbnail thumbnail = thumbnailMapper.selectByPrimaryKey(thumbnailId);
        String srcFilename = thumbnail.getFileName();
        String suffix = "";
        int index = srcFilename.lastIndexOf('.');
        if (index > 0) {
            suffix = srcFilename.substring(index);
        }
        //下载文件到临时目录
        minIODownload(bucketName, thumbnail.getUrl(), path, fileName + suffix);
    }

    /**
     * 压缩包下载
     * @param zipDir 压缩目录
     *  @param zipPath 压缩包生成路径
     * @param zipName 压缩包名称
     * @throws Exception
     */
    public void zipDownload(String zipDir, String zipPath, String zipName) throws Exception {
        FileOutputStream fos1 = new FileOutputStream(new File(zipPath + "/" + zipName));
        ZIPUtil.toZip(zipDir, fos1, false);
        //下载
        File file = new File(zipPath + "/" + zipName);
        try (InputStream in = new FileInputStream(file);
             OutputStream stream = response.getOutputStream()) {
            //解决编码问题
            zipName = URLEncoder.encode(zipName, "UTF-8");
            response.setContentType("multipart/form-data; charset=utf-8");
            //设置响应头，把文件名字设置好
            response.setHeader("Content-disposition", "attachment;filename=" + zipName);
            //输出流开始写出文件
            byte[] b = new byte[1024];
            int len;
            while ((len = in.read(b)) > 0) {
                stream.write(b, 0, len);
            }
            //刷新流
            stream.flush();
        }
        //删除临时压缩包
        Files.delete(file.toPath());
        //删除临时文件
        FileUtil.deleteAllFilesOfDir(new File(zipDir));
    }

    /**
     * minIO下载(下载到指定文件夹)
     * @param bucketName
     * @param keyName
     * @param filePath
     * @param fileName
     * @throws Exception
     */
    public void minIODownload(String bucketName,
                              String keyName, String filePath, String fileName) throws Exception {
        S3Object o = amazonS3.getObject(bucketName, keyName);
        //初始化路径和文件
        File folder = new File(filePath);
        if (!folder.exists() && !folder.isDirectory()) {
            folder.mkdirs();
        }
        File file = new File(filePath + "/" + fileName);
        if (!file.exists()) {
            file.createNewFile();
        }
        //下载到指定文件夹
        try (S3ObjectInputStream s3is = o.getObjectContent();
             OutputStream stream = new FileOutputStream(filePath + "/" + fileName)) {
            byte[] readBbuf = new byte[1024];
            int readLen = 0;
            //输出流开始写出文件
            while ((readLen = s3is.read(readBbuf)) > 0) {
                stream.write(readBbuf, 0, readLen);
            }
            //刷新流
            stream.flush();
        }
    }

    /**
     * minIO下载(返回到response)
     * @param keyName
     * @param bucketName
     * @throws Exception
     */
    public void minIODownload(String bucketName,
                              String keyName, String fileName) throws Exception {
        S3Object o = amazonS3.getObject(bucketName, keyName);
        try (S3ObjectInputStream s3is = o.getObjectContent();
             OutputStream stream = response.getOutputStream()) {
            byte[] readBbuf = new byte[1024];
            int readLen = 0;
            //解决编码问题
            fileName = URLEncoder.encode(fileName, "UTF-8");
            response.setContentType("multipart/form-data; charset=utf-8");
            //设置响应头，把文件名字设置好
            response.setHeader("Content-disposition", "attachment;filename=" + fileName);
            //输出流开始写出文件
            while ((readLen = s3is.read(readBbuf)) > 0) {
                stream.write(readBbuf, 0, readLen);
            }
            //刷新流
            stream.flush();
        }
    }
}
