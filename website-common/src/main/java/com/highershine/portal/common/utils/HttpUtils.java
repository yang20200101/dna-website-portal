package com.highershine.portal.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * @Deprecated: HttpClient 远程调用工具类
 * @ClassName: HttpUtils
 * @Author: highershine.mizhanlei
 * @Date: 2018/12/28 10:13
 * @Motified: 2018/12/28 10:13 by highershine.mizhanlei
 */
@Slf4j
public class HttpUtils {

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) throws Exception {
        log.info("http连接开始：" + DateTools.getNowTimeStr());
        log.info("http url是：" + url);
        log.info("http 参数是：" + param);
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            URLConnection conn = realUrl.openConnection(); //打开和URL之间的连接
            conn.setConnectTimeout(60000);
            conn.setRequestProperty("accept", "*/*"); // 设置通用的请求属性
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setDoOutput(true); // 发送POST请求必须设置如下两行
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            log.info("http连接结束：" + DateTools.getNowTimeStr());
        } catch (Exception e) {
            log.info("http连接失败：" + DateTools.getNowTimeStr());
            log.error("sendPost", e);
            log.error("发送 POST 请求出现异常！");
            throw new Exception("发送 POST 请求出现异常！");
        } finally { //使用finally块来关闭输出流、输入流
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                log.error("sendPost", ex);
            }
        }
        return result;
    }

    /**
     * httpClient 发送post请求
     *
     * @param url
     * @param params
     * @return
     */
    @SuppressWarnings({"deprecation", "rawtypes", "unchecked"})
    public static String doPost(String url, Map params) {
        BufferedReader in = null;

        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost request = new HttpPost();
            request.setURI(new URI(url));
            List<NameValuePair> nvps = new ArrayList();
            Iterator iter = params.keySet().iterator();

            while (iter.hasNext()) {
                String name = (String) iter.next();
                String value = String.valueOf(params.get(name));
                nvps.add(new BasicNameValuePair(name, value));
            }

            request.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
            HttpResponse response = client.execute(request);
            int code = response.getStatusLine().getStatusCode();
            if (code != 200) {
                log.error("【POST请求】请求异常，request_url:{}, HTTPStatus：{}", url, code);
                return null;
            } else {
                in = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
                StringBuffer sb = new StringBuffer("");
                String line = "";
                String nl = System.getProperty("line.separator");

                while ((line = in.readLine()) != null) {
                    sb.append(line + nl);
                }

                in.close();
                return sb.toString();
            }
        } catch (Exception var11) {
            var11.printStackTrace();
            return null;
        }
    }

    /**
     * httpclient实现文件上传post请求
     *
     * @param url
     * @param contentType
     * @param params
     * @return
     */
    public static String doPostOfMultipart(String url, String contentType, Map<String, Object> params, Map<String, File> fileparams) {
        BufferedReader in = null;
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);
            Iterator iter = params.keySet().iterator();
            Iterator fileIter = fileparams.keySet().iterator();
            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
            while (iter.hasNext()) {
                String name = (String) iter.next();
                String value = String.valueOf(params.get(name));
//                ContentType  contentType = ContentType.create("text/plain",Charset.forName("UTF-8"));
//                entityBuilder.addTextBody(name,value).setCharset(Consts.UTF_8);
                entityBuilder.addPart(name, new StringBody(value, Charset.forName("UTF-8")));
            }
            while (fileIter.hasNext()) {
                String name = (String) fileIter.next();
                File file = fileparams.get(name);
                entityBuilder.addPart(name, new FileBody(file));
            }
            HttpEntity httpEntity = entityBuilder.build();
            httpPost.setEntity(httpEntity);
            HttpResponse response = httpClient.execute(httpPost);
            int code = response.getStatusLine().getStatusCode();
          /*  if (code != 200) {
                log.error("【doPostOfMultipart请求】请求异常，request_url:{}, HTTPStatus：{}", url, code);
                return null;
            } else {*/
            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
            StringBuffer sb = new StringBuffer("");
            String line = "";
            String nl = System.getProperty("line.separator");

            while ((line = in.readLine()) != null) {
                sb.append(line + nl);
            }

            in.close();
            return sb.toString();
            /*  }*/
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }
}
