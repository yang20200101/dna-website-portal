package com.highershine.portal.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

@Slf4j
public class URLConnectionUtil {
    /**
     * 可以设置header
     *
     * @param strUrl
     * @param map
     * @return
     * @throws Exception
     */
    public static String get(String strUrl, Map<String, String> map) throws Exception {
        StringBuilder result = new StringBuilder();
        BufferedReader in = null;
        try {
            URL url = new URL(strUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setRequestProperty("connection", "Keep-Alive");
            if (map != null && map.size() > 0) {
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    conn.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }

            conn.connect();
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    conn.getInputStream(), "utf-8"));
            String strLine = null;
            while ((strLine = in.readLine()) != null) {
                result.append(strLine);
            }
        } catch (Exception e) {
            log.error("发送 Get 方法异常！{},{}", strUrl, e);
            throw e;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                log.error("关闭流异常", ex);
            }
        }
        return result.toString();
    }

    /**
     * POST请求，json串形式数据
     *
     * @param url
     *            请求地址
     * @param json
     *            请求数据
     * @throws Exception
     */
    public static String post(String url, String json) throws Exception {
        PrintWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            // 设置通用的请求属性
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            conn.setUseCaches(false);
            conn.setInstanceFollowRedirects(true);
            conn.connect();
            // 获取URLConnection对象对应的输出流
            OutputStreamWriter outWriter = new OutputStreamWriter(conn.getOutputStream(), "utf-8");
            out = new PrintWriter(outWriter);
            // 发送请求参数
            out.print(json);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            log.error("发送 POST 请求出现异常！", e);
            throw e;
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result.toString();
    }

    /**
     * POST请求，json串形式数据
     *
     * @param url
     *            请求地址
     * @param json
     *            请求数据
     * @param cookie
     *            cookie
     * @throws Exception
     */
    public static String post(String url, String json, String cookie) throws Exception {
        PrintWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            // 设置通用的请求属性
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            conn.addRequestProperty("Content-Type", "application/json;charset=utf-8");
            if (StringUtils.isNotBlank(cookie)) {
                conn.setRequestProperty("Cookie", cookie);
            }
            conn.setUseCaches(false);
            conn.setInstanceFollowRedirects(true);
            conn.connect();
            // 获取URLConnection对象对应的输出流
            OutputStreamWriter outWriter = new OutputStreamWriter(conn.getOutputStream(), "utf-8");
            out = new PrintWriter(outWriter);
            // 发送请求参数
            out.print(json);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            throw e;
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result.toString();
    }

    public static InputStream getImageStream(String urlStr, String postData) throws Exception {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn;
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            conn.setUseCaches(false);
            conn.setInstanceFollowRedirects(true);
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            DataOutputStream out = new DataOutputStream(
                    conn.getOutputStream());
            if (postData != null) {
                out.writeBytes(postData);
            }
            out.flush();
            out.close();
            return conn.getInputStream();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

}