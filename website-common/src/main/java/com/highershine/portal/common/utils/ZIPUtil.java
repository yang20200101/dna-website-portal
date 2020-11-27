package com.highershine.portal.common.utils;

import com.highershine.portal.common.constants.CommonConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.io.*;
import java.nio.file.Files;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.springframework.util.StreamUtils.BUFFER_SIZE;

/**
 * @Description: ZIP工具类
 * @Author: xueboren
 * @Date: 2020/1/13 10:00
 */
@Slf4j
public final class ZIPUtil {
    private ZIPUtil() {

    }

    /**
     * 压缩成ZIP 方法     * @param srcDir 压缩文件夹路径
     *
     * @param out              压缩文件输出流
     * @param keepDirStructure 是否保留原来的目录结构,true:保留目录结构;
     *                         false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
     * @throws RuntimeException 压缩失败会抛出运行时异常
     */
    public static void toZip(String srcDir, OutputStream out, boolean keepDirStructure)
            throws Exception {
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(out);
            File sourceFile = new File(srcDir);
            compress(sourceFile, zos, sourceFile.getName(), keepDirStructure);
        } catch (Exception e) {
            throw new RuntimeException("zip error from ZipUtil", e);
        } finally {
            if (zos != null) {
               zos.close();
            }
            if (out != null) {
               out.close();
            }
        }

    }

    /**
     * 递归压缩方法
     *
     * @param sourceFile       源文件
     * @param zos              zip输出流
     * @param name             压缩后的名称
     * @param keepDirStructure 是否保留原来的目录结构,true:保留目录结构;
     *                         false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
     * @throws Exception
     */
    private static void compress(File sourceFile, ZipOutputStream zos, String name,
                                 boolean keepDirStructure) throws Exception {
        byte[] buf = new byte[BUFFER_SIZE];
        if (sourceFile.isFile()) {
            // 向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字
            zos.putNextEntry(new ZipEntry(name));
            // copy文件到zip输出流中
            int len;
            FileInputStream in = new FileInputStream(sourceFile);
            while ((len = in.read(buf)) != -1) {
                zos.write(buf, 0, len);
            }
            // Complete the entry
            zos.closeEntry();
            in.close();
        } else {
            //是文件夹
            File[] listFiles = sourceFile.listFiles();
            if (listFiles == null || listFiles.length == 0) {
                // 需要保留原来的文件结构时,需要对空文件夹进行处理
                if (keepDirStructure) {
                    // 空文件夹的处理
                    zos.putNextEntry(new ZipEntry(name + "/"));
                    // 没有文件，不需要文件的copy
                    zos.closeEntry();
                }

            } else {
                for (File file : listFiles) {
                    // 判断是否需要保留原来的文件结构
                    if (keepDirStructure) {
                        // 注意：file.getName()前面需要带上父文件夹的名字加一斜杠,
                        // 不然最后压缩包中就不能保留原来的文件结构,即：所有文件都跑到压缩包根目录下了
                        compress(file, zos, name + "/" + file.getName(), keepDirStructure);
                    } else {
                        compress(file, zos, file.getName(), keepDirStructure);
                    }

                }
            }
        }
    }

    /**
     * 创建Zip文件
     *
     * @param fileList    要压缩的文件路径
     * @param zipFileName ZIP压缩包的路径
     * @return ZIP压缩包的路径
     */
    public static String createZipFile(List<String> fileList, String zipFileName) throws IOException {
        if (CollectionUtils.isEmpty(fileList) || StringUtils.isBlank(zipFileName)) {
            return null;
        }
        //构建压缩文件File
        File zipFile = new File(zipFileName);
        //构建ZIP流对象
        try (ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFile))) {
            //循环处理传过来的集合
            for (int i = 0; i < fileList.size(); i++) {
                //获取目标文件
                File inFile = new File(fileList.get(i));
                if (inFile.exists()) {
                    //定义ZipEntry对象
                    ZipEntry entry = new ZipEntry(inFile.getName());
                    //赋予ZIP流对象属性
                    out.putNextEntry(entry);
                    int len = 0;
                    //缓冲
                    byte[] buffer = new byte[1024];
                    //构建FileInputStream流对象
                    try (FileInputStream fis = new FileInputStream(inFile)) {
                        while ((len = fis.read(buffer)) > 0) {
                            out.write(buffer, 0, len);
                            out.flush();
                        }
                    }
                    //关闭closeEntry
                    out.closeEntry();
                }
            }
        }
        return zipFileName;
    }

    /**
     * 创建Zip文件
     *
     * @param contentList 要压缩的文件二进制
     * @return ZIP压缩包二进制
     */
    public static byte[] createZipByte(List<Map<String, Object>> contentList) throws IOException {
        String filePath = CommonConstant.TMP_PATH;
        String zipFileName = "tmp" + DateTools.dateToString(new Date(), DateTools.DF_COMPACT_TIME) + ".zip";
        //构建压缩文件File
        File zipFile = new File(filePath + "/" + zipFileName);
        //构建ZIP流对象
        try (ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFile))) {
            //循环处理传过来的集合
            for (Map<String, Object> map : contentList) {
                String fileName = (String) map.get("fileName");
                byte[] bytes = (byte[]) map.get("byte");
                //定义ZipEntry对象
                ZipEntry entry = new ZipEntry(fileName);
                //赋予ZIP流对象属性
                out.putNextEntry(entry);
                out.write(0);
                out.flush();
                //关闭closeEntry
                out.closeEntry();
            }
        }
        return file2byte(zipFile);
    }

    /**
     * 将文件File转换成byte数组
     *
     * @param tradeFile
     * @return
     */
    private static byte[] file2byte(File tradeFile) throws IOException {
        byte[] buffer = null;
        try (FileInputStream fis = new FileInputStream(tradeFile);
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            buffer = bos.toByteArray();
        }
        Files.delete(tradeFile.toPath());
        return buffer;
    }
}
