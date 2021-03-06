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
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
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
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;
    @Autowired
    private ThumbnailMapper thumbnailMapper;
    @Autowired
    private ActivityMapper activityMapper;
    @Autowired
    private SysRegionalismService regionalismService;

    /**
     * ??????
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
        //????????????
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
        //?????????????????????
        Activity activity = activityMapper.selectByPrimaryKey(activityId);
        Application application = new Application();
        application.setActivityId(activityId).setIsLatest(true);
        List<ActivityUserVo> list = activityMapper.getActivityUserList(application);
        //???????????? ????????????
        String zipName = StringUtil.dealFileName(activity.getTitle())
                + DateTools.dateToString(activity.getDate());
        String zipPath = CommonConstant.TMP_PATH + "/"  + zipName;
        //??????????????????
        for (ActivityUserVo activityUserVo : list) {
            getMaterialFile(zipPath, activityUserVo.getNickname() +
                            "(" + regionalismService.getNameByCode(activityUserVo.getProvince())+ ")",
                    bucketName, activityUserVo.getThumbnailId());
        }
        //??????????????? ?????????
        zipDownload(zipPath, CommonConstant.TMP_PATH, zipName + ".zip");
    }

    @Override
    public void download(String bucketName, Long id) throws Exception {
        Thumbnail thumbnail = thumbnailMapper.selectByPrimaryKey(id);
        if (StringUtils.isNotBlank(request.getParameter("filename"))) {
            minIODownload(bucketName, thumbnail.getUrl(), request.getParameter("filename"));
        } else {
            minIODownload(bucketName, thumbnail.getUrl(), thumbnail.getFileName());
        }
    }

    /**
     * ?????????????????????
     * @param path ??????
     * @param fileName ?????????
     * @param bucketName
     * @param thumbnailId
     * @throws Exception
     */
    private void getMaterialFile(String path, String fileName, String bucketName, Long thumbnailId) throws Exception {
        Thumbnail thumbnail = thumbnailMapper.selectByPrimaryKey(thumbnailId);
        if (thumbnail != null) {
            String srcFilename = thumbnail.getFileName();
            String suffix = "";
            int index = srcFilename.lastIndexOf('.');
            if (index > 0) {
                suffix = srcFilename.substring(index);
            }
            //???????????????????????????
            minIODownload(bucketName, thumbnail.getUrl(), path, fileName + suffix);
        }
    }

    /**
     * ???????????????
     * @param zipDir ????????????
     *  @param zipPath ?????????????????????
     * @param zipName ???????????????
     * @throws Exception
     */
    public void zipDownload(String zipDir, String zipPath, String zipName) throws Exception {
        FileOutputStream fos1 = new FileOutputStream(new File(zipPath + "/" + zipName));
        ZIPUtil.toZip(zipDir, fos1, false);
        //??????
        File file = new File(zipPath + "/" + zipName);
        try (InputStream in = new FileInputStream(file);
             OutputStream stream = response.getOutputStream()) {
            //??????????????????
            zipName = URLEncoder.encode(zipName, "UTF-8");
            response.setContentType("multipart/form-data; charset=utf-8");
            //??????????????????????????????????????????
            response.setHeader("Content-disposition", "attachment;filename=" + zipName);
            //???????????????????????????
            byte[] b = new byte[1024];
            int len;
            while ((len = in.read(b)) > 0) {
                stream.write(b, 0, len);
            }
            //?????????
            stream.flush();
        }
        //?????????????????????
        Files.delete(file.toPath());
        //??????????????????
        FileUtil.deleteAllFilesOfDir(new File(zipDir));
    }

    /**
     * minIO??????(????????????????????????)
     * @param bucketName
     * @param keyName
     * @param filePath
     * @param fileName
     * @throws Exception
     */
    public void minIODownload(String bucketName,
                              String keyName, String filePath, String fileName) throws Exception {
        S3Object o = amazonS3.getObject(bucketName, keyName);
        //????????????????????????
        File folder = new File(filePath);
        if (!folder.exists() && !folder.isDirectory()) {
            folder.mkdirs();
        }
        File file = new File(filePath + "/" + fileName);
        if (!file.exists()) {
            file.createNewFile();
        }
        //????????????????????????
        try (S3ObjectInputStream s3is = o.getObjectContent();
             OutputStream stream = new FileOutputStream(filePath + "/" + fileName)) {
            byte[] readBbuf = new byte[1024];
            int readLen = 0;
            //???????????????????????????
            while ((readLen = s3is.read(readBbuf)) > 0) {
                stream.write(readBbuf, 0, readLen);
            }
            //?????????
            stream.flush();
        }
    }

    /**
     * minIO??????(?????????response)
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
            //??????????????????
            fileName = URLEncoder.encode(fileName, "UTF-8");
            response.setContentType("multipart/form-data; charset=utf-8");
            //??????????????????????????????????????????
            response.setHeader("Content-disposition", "attachment;filename=" + fileName);
            //???????????????????????????
            while ((readLen = s3is.read(readBbuf)) > 0) {
                stream.write(readBbuf, 0, readLen);
            }
            //?????????
            stream.flush();
        }
    }
}
