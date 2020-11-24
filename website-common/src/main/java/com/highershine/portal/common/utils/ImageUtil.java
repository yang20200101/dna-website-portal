package com.highershine.portal.common.utils;

import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ImageUtil {
    public static final  String DEFAULT_PREVFIX = "thumb_";
    //建议该值为falses
    private static final  Boolean DEFAULT_FORCE = false;
    public static final  String SUCCESS_CODE = "1";
    public static final  String FAILURE_CODE = "0";

    /**
     * <p>Title: thumbnailImage</p>
     * <p>Description: 依据图片路径生成缩略图 </p>
     *
     * @param imgFile 原图片路径
     * @param w         缩略图宽
     * @param h         缩略图高
     * @param prevfix   生成缩略图的前缀
     * @param force     是否强制依照宽高生成缩略图(假设为false，则生成最佳比例缩略图)
     */
    public static Map<String, Object> thumbnailImage(File imgFile, int w, int h, String prevfix, boolean force) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("rtnCode", FAILURE_CODE);
        String imageName = imgFile.getName();
        if (imgFile.exists()) {
            try {
                // ImageIO 支持的图片类型 : [BMP, bmp, jpg, JPG, wbmp, jpeg, png, PNG, JPEG, WBMP, GIF, gif]
                String types = Arrays.toString(ImageIO.getReaderFormatNames());
                String suffix = null;
                // 获取图片后缀
                if (imageName.indexOf(".") > -1) {
                    suffix = imageName.substring(imageName.lastIndexOf(".") + 1);
                }
                // 类型和图片后缀所有小写，然后推断后缀是否合法
                if (suffix == null || types.toLowerCase().indexOf(suffix.toLowerCase()) < 0) {
                    result.put("message", "图片格式为:" + suffix + ",不支持此格式");
                    return result;
                }
                Image img = ImageIO.read(imgFile);
                if (!force) {
                    // 依据原图与要求的缩略图比例，找到最合适的缩略图比例
                    int width = img.getWidth(null);
                    int height = img.getHeight(null);
                    if ((width * 1.0) / w < (height * 1.0) / h) {
                        if (width > w) {
                            h = Integer.parseInt(new java.text.DecimalFormat("0").format(height * w / (width * 1.0)));
                        }
                    } else {
                        if (height > h) {
                            w = Integer.parseInt(new java.text.DecimalFormat("0").format(width * h / (height * 1.0)));
                        }
                    }
                }
                BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
                Graphics g = bi.getGraphics();
                g.drawImage(img, 0, 0, w, h, Color.LIGHT_GRAY, null);
                g.dispose();
                String p = imgFile.getPath();
                String thumbPath = "";
                if (p.lastIndexOf(File.separator) > 0) {
                     thumbPath = p.substring(0, p.lastIndexOf(File.separator)) + File.separator + prevfix + imageName;
                } else {
                    thumbPath = prevfix + imageName;
                }
                // 将图片保存在原文件夹并加上前缀
                boolean isSuccess = ImageIO.write(bi, suffix, new File(thumbPath));
                if (isSuccess) {
                    result.put("rtnCode", SUCCESS_CODE);
                    result.put("thumbPath", thumbPath);
                    return result;
                } else {
                    result.put("message", "缩略图生成失败");
                    return result;
                }

            } catch (IOException e) {
                log.error("生成图片缩略图异常", e);
                result.put("message", "缩略图生成失败");
            }
        } else {
            result.put("message", "原图不存在");
        }
        return result;
    }

    public static Map<String, Object> generateThumbImage(File originFile) {
        return thumbnailImage(originFile, 100, 150, DEFAULT_PREVFIX, DEFAULT_FORCE);
    }
}
