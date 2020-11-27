package com.highershine.portal.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.highershine.portal.common.constants.CommonConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

@Slf4j
public class FileUtil {
    /**
     * MultipartFile 转 File
     * @param file
     * @throws Exception
     */
    public static File multipartFileToFile(MultipartFile file) throws Exception {
        File toFile = null;
        if (file.equals("") || file.getSize() <= 0) {
            file = null;
        } else {
            InputStream ins = null;
            ins = file.getInputStream();
            toFile = new File(CommonConstant.TMP_PATH + "/" + file.getOriginalFilename());
            inputStreamToFile(ins, toFile);
            ins.close();
        }
        return toFile;
    }

    //获取流文件
    private static void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除本地临时文件
     * @param file
     */
    public static void delteTempFile(File file) {
        if (file != null) {
            File del = new File(file.toURI());
            del.delete();
        }
    }

    /**
     * 删除文件夹及其子文件
     * @param path
     */
    public static void deleteAllFilesOfDir(File path) {
        if (!path.exists() || path.isFile()) {
            if (path.isFile()) {
                path.delete();
            }
            return;
        }

        File[] files = path.listFiles();
        for (int i = 0; i < files.length; i++) {
            deleteAllFilesOfDir(files[i]);
        }
        path.delete();
    }

    /**
     * 判断目录是否存在，不存在则创建
     * @param path
     */
    public static void checkDirExists(String path) {
        File file = new File(path);
        if  (!file .exists()  && !file .isDirectory()) {
            file .mkdirs();
        }
    }


    /**
     * 下载文件
     * @param filePath
     * @param fileName
     * @throws IOException
     */
    public static void downloadFile(HttpServletResponse response, String filePath, String fileName) throws IOException {
        File file = new File(filePath + "/" + fileName);
        try (FileInputStream in = new FileInputStream(file);
             OutputStream out = response.getOutputStream();) {
            response.setContentType("multipart/form-data; charset=utf-8");
            fileName = URLEncoder.encode(fileName, "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName);
            byte[] d = new byte[256];
            int count = 0;
            while ((count = in.read(d)) != -1) {
                out.write(d, 0, count);
            }
            out.flush();
        }
    }

    /**
     * 下载文件
     * @param filePath
     * @param fileName
     * @throws IOException
     */
    public static void downloadFile(HttpServletResponse response, String filePath, String fileName, String rename) throws IOException {
        File file = new File(filePath + "/" + fileName);
        try (FileInputStream in = new FileInputStream(file);
             OutputStream out = response.getOutputStream();) {
            response.setContentType("multipart/form-data; charset=utf-8");
            rename = URLEncoder.encode(rename, "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + rename);
            byte[] d = new byte[256];
            int count = 0;
            while ((count = in.read(d)) != -1) {
                out.write(d, 0, count);
            }
            out.flush();
        }
    }

    /**
     * 生成json文件
     * @param filePath
     * @param obj
     */
    public static void createJsonFile(String filePath, Object obj) {
        JSONObject json = (JSONObject) JSONObject.toJSON(obj);
        BufferedWriter out = null;
        try {
            File file = new File(filePath);
            // 创建新文件
            file.createNewFile();
            out = new BufferedWriter(new FileWriter(file));
            out.write(json.toString());
        } catch (Exception e) {
            log.error("生成json文件异常，异常信息：", e);
        } finally {
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String linuxCmd(String str) {
        String[] cmd = new String[]{"sh", "-c", str};
        Runtime run = Runtime.getRuntime();
        try {
            Process process = run.exec(cmd);
            String line;
            BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuffer out = new StringBuffer();
            while ((line = stdoutReader.readLine()) != null) {
                out.append(line);
            }
            try {
                process.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            process.destroy();
            return out.toString();
        } catch (IOException e) {
        }
        return null;
    }

    /**
     * 获取文件后缀名
     * @param fileName
     * @return
     */
    public static String getSuffix(String fileName) {
        //文件后缀
        String suffix = "";
        int index = fileName.lastIndexOf('.');
        if (index > 0) {
            suffix = fileName.substring(index);
        }
        return suffix;
    }
}
