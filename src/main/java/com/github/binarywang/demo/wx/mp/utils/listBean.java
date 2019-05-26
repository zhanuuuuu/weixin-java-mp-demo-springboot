package com.github.binarywang.demo.wx.mp.utils;




import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.github.binarywang.demo.wx.mp.utils.ReflexObjectUtil.getKeysAndValues;


/**
 * Created by Administrator on 2018-03-17.
 */
public class listBean {

    //通用方法    list map 转换成json对象
    @SuppressWarnings("unchecked")
    public static String getBeanJson(List list){
        List<Map<String, Object>> list1=getKeysAndValues(list);
        List list2 =new ArrayList();
        for(Map<String, Object> map:list1){
            JSONObject jsonObject = new JSONObject(map);
            list2.add(jsonObject);
        }
        return list2.toString();
    }


}
