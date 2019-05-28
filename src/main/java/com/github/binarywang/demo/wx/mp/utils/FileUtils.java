package com.github.binarywang.demo.wx.mp.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.MimetypesFileTypeMap;
import java.io.*;

public class FileUtils {

    private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    public static void main(String[] args) {
        String str = readJSONFromResource("cs/test.json");
        System.out.println(str);
    }

    public static String getFilePathByName(String fileName) {
        try {
            String filePath = FileUtils.class.getClassLoader().getResource(fileName).getPath();
            return filePath;
        } catch (Exception e) {
            logger.error(fileName + "is not exist in the resources folder");
        }
        return null;
    }

    public static String readJSONFromResource(String fileName) {
        return readFileFromResource(fileName).replace(" ", "");
    }


    public static String readFileFromResource(String fileName) {
        String resultStr = "";
        String path = getFilePathByName(fileName);
        if (path == null) {
            return null;
        }
        File file = new File(path);
        BufferedReader reader = null;
        try {
            FileInputStream in = new FileInputStream(file);
            reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                resultStr = resultStr + tempString;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException el) {
                    el.printStackTrace();
                }
            }
        }
        return resultStr;
    }

    public static String getMimeType(File file) {
        return new MimetypesFileTypeMap().getContentType(file);
    }

}
