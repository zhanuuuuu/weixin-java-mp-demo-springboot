package com.github.binarywang.demo.wx.mp.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HttpUtils {
    private static final Logger log = LoggerFactory.getLogger(HttpUtils.class);

    /**
     * 发送 post请求访问本地应用并根据传递参数不同返回不同结果
     */
    /**
     * 发送 post请求访问本地应用并根据传递参数不同返回不同结果
     */
    public static String post(String uri, String body) throws Exception {
        // 创建默认的httpClient实例.
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 创建httppost
        HttpPost httpPost = new HttpPost(uri);
        // 创建参数队列
        /*List<NameValuePair> params = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("type", "house"));
        UrlEncodedFormEntity uefEntity;*/
        String result;
        try {
           /* uefEntity = new UrlEncodedFormEntity(params, "UTF-8");
            httpPost.setEntity(uefEntity);*/

            httpPost.setHeader("Content-type", "application/json");
            StringEntity stringEntity = new StringEntity(body, "UTF-8");
            httpPost.getRequestLine();
            httpPost.setEntity(stringEntity);
            System.out.println("executing request " + httpPost.getURI());
            CloseableHttpResponse response = httpclient.execute(httpPost);
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    System.out.println("--------------------------------------");
                    result = EntityUtils.toString(entity, "UTF-8");
                    System.out.println("response " + result);
                    System.out.println("--------------------------------------");
                    return result;
                } else {
                    throw new NullPointerException("返回结果为空");
                }
            } finally {
                response.close();
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getLocalizedMessage());
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
            throw new NullPointerException(e1.getLocalizedMessage());
        } catch (IOException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getLocalizedMessage());
        } finally {
            // 关闭连接,释放资源
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 发送 get请求
     */
    public void get() {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            // 创建httpget.
            HttpGet httpget = new HttpGet("http://www.baidu.com/");
            System.out.println("executing request " + httpget.getURI());
            // 执行get请求.
            CloseableHttpResponse response = httpclient.execute(httpget);
            try {
                // 获取响应实体
                HttpEntity entity = response.getEntity();
                System.out.println("--------------------------------------");
                // 打印响应状态
                System.out.println(response.getStatusLine());
                if (entity != null) {
                    // 打印响应内容长度
                    System.out.println("Response content length: " + entity.getContentLength());
                    // 打印响应内容
                    System.out.println("Response content: " + EntityUtils.toString(entity));
                }
                System.out.println("------------------------------------");
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接,释放资源
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    //模拟文件发送   这里需要文件地址  url
    public static String postImageStream(String url, String filePath) throws IOException {
        log.info("------------------------------HttpClient POST开始-------------------------------");
        log.info("POST:" + url);
        log.info("filePath:" + filePath);
        if (StringUtils.isBlank(url)) {
            log.error("post请求不合法，请检查uri参数!");
            return null;
        }
        StringBuilder content = new StringBuilder();

        // 模拟表单上传 POST 提交主体内容
        String boundary = "-----------------------------" + new Date().getTime();
        // 待上传的文件
        File file = new File(filePath);

        if (!file.exists() || file.isDirectory()) {
            log.error(filePath + ":不是一个有效的文件路径");
            return null;
        }

        // 响应内容
        String respContent = null;

        InputStream is = null;
        OutputStream os = null;
        BufferedInputStream bis = null;
        File tempFile = null;
        CloseableHttpClient httpClient = null;
        HttpPost httpPost = null;
        try {
            // 创建临时文件，将post内容保存到该临时文件下，临时文件保存在系统默认临时目录下，使用系统默认文件名称
            tempFile = File.createTempFile(new SimpleDateFormat("yyyy_MM_dd").format(new Date()), null);
            os = new FileOutputStream(tempFile);
            is = new FileInputStream(file);

            os.write(("--" + boundary + "\r\n").getBytes());
            os.write(String.format(
                    "Content-Disposition: form-data; name=\"media\"; filename=\"" + file.getName() + "\"\r\n")
                    .getBytes());
            os.write(String.format("Content-Type: %s\r\n\r\n", FileUtils.getMimeType(file)).getBytes());

            // 读取上传文件
            bis = new BufferedInputStream(is);
            byte[] buff = new byte[8096];
            int len = 0;
            while ((len = bis.read(buff)) != -1) {
                os.write(buff, 0, len);
            }

            os.write(("\r\n--" + boundary + "--\r\n").getBytes());

            httpClient = HttpClients.createDefault();
            // 创建POST请求
            httpPost = new HttpPost(url);

            // 创建请求实体
            FileEntity reqEntity = new FileEntity(tempFile, ContentType.MULTIPART_FORM_DATA);

            // 设置请求编码
            reqEntity.setContentEncoding("UTF-8");
            httpPost.setEntity(reqEntity);
            // 执行请求
            HttpResponse response = httpClient.execute(httpPost);
            // 获取响应内容
            respContent = EntityUtils.toString(response.getEntity());
            if(respContent.startsWith("code")) {
                log.info("resp：" + respContent);
                throw new RuntimeException("请求失败，请检查URL地址和请求参数...");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                bis.close();
            }

            if (is != null) {
                is.close();
            }

            if (os != null) {
                os.close();
            }

            if (httpPost != null) {
                httpPost.releaseConnection();
            }

            if (httpClient != null) {
                httpClient.close();
            }
        }
        log.info("resp：" + respContent);
        log.info("------------------------------HttpClient POST结束-------------------------------");
        return respContent;
    }


}
