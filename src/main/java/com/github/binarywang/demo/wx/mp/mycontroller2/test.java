package com.github.binarywang.demo.wx.mp.mycontroller2;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.client.RestClientException;

/**
 * Created by Administrator on 2019-05-29.
 */
public class test {

    public static void main(String[] args) {

        System.out.println(testExcept());

    }

    public static String  testExcept(){

        String result="出错了";
        try{
            result=String.valueOf(Integer.valueOf("p"));
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
}
