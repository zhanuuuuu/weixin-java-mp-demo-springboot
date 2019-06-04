package com.github.binarywang.demo.wx.mp.utils;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadPicFromURL {
    public static void main(String[] args) {
        String url = "http://localhost:1237/GYL/download";
        url = "http://mmbiz.qpic.cn/mmbiz_png/8vPUZq7OF0ibsfj8pIF5XMubaOGbicZHVg2CiarG4kMvJlLz6rLgKSp3DZibpULmibGJlLRPiaUe7a1PpXic2NaAEWLJg/0";
        String path="D:/pic99.png";
        downloadPicture(url,path);
    }
    //链接url下载图片
    public static void downloadPicture(String urlList,String path) {
        URL url = null;
        try {
            url = new URL(urlList);
            DataInputStream dataInputStream = new DataInputStream(url.openStream());
            File file = new File(path);
            if(!file.exists()){
                //先得到文件的上级目录，并创建上级目录，在创建文件
                file.getParentFile().mkdir();
                try {
                    //创建文件
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int length;

            while ((length = dataInputStream.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            fileOutputStream.write(output.toByteArray());
            dataInputStream.close();
            fileOutputStream.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new  RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new  RuntimeException(e);
        }
    }
}

