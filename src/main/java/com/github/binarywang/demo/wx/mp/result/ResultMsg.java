package com.github.binarywang.demo.wx.mp.result;

import com.github.binarywang.demo.wx.mp.domain.User;
import com.github.binarywang.demo.wx.mp.utils.JsonUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2019-05-24.
 */
public class ResultMsg implements Serializable {

    private String code;

    private String message;

    private Object result;


    public ResultMsg(String code, String message, Object result) {
        this.code = code;
        this.message = message;
        this.result = result;
    }

    public ResultMsg(GlobalInfo globalInfo, Object result) {
        this.code = globalInfo.getCode()+"";
        this.message = globalInfo.getMesssage();
        this.result = result;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "resultMsg{" +
            "code='" + code + '\'' +
            ", message='" + message + '\'' +
            ", result=" + result +
            '}';
    }

    public static String ResultMsg(GlobalInfo globalInfo, Object var0){
        String result="";
        try{
            result=JsonUtils.toJson(new ResultMsg(globalInfo,var0));
        }catch (Exception e){
            result=JsonUtils.toJson(new ResultMsg(GlobalEumn.JSON_ANALYZING_FAIL,"[]"));
        }
        return result;
    }

    /**
     *
     * @param var0  需要返回的数据结果集
     * @return      统一返回封装类
     */
    public static String ResultMsg(String var0){
        String result="";
        try{
            if (var0!=null) {
                Pattern p = Pattern.compile("\\s*|\t|\r|\n");
                Matcher m = p.matcher(var0);
                var0 = m.replaceAll("");
            }
            result=JsonUtils.toJson(new ResultMsg(GlobalEumn.SUCCESS,var0));
        }catch (Exception e){
            result=JsonUtils.toJson(new ResultMsg(GlobalEumn.JSON_ANALYZING_FAIL,"[]"));
        }
        return result;
    }

    public static String ResultMsg(GlobalInfo globalInfo){
        return JsonUtils.toJson(new ResultMsg(globalInfo,"[]"));
    }

    public static void GetTypeClass(String var0){
        if(var0.getClass().toString().equals("class java.lang.String")){

            if (var0!=null) {
                Pattern p = Pattern.compile("\\s*|\t|\r|\n");
                Matcher m = p.matcher(var0);
                var0 = m.replaceAll("");
            }

            System.out.println(var0);
        } else {
            System.out.println("我不是字符串类型");
        }
    }

    public static void main(String[] args) {
        User user=null;
        List<User> list=new ArrayList<>();
        user=new User("张明阳","http://12345799//44448975","男","中国");
        list.add(user);
        user=new User("张明阳2","http://12345799//44448975","男","中国");
        list.add(user);
        user=new User("张明阳3","http://12345799//44448975","男","中国");
        list.add(user);
        System.out.println(ResultMsg(JsonUtils.toJson(list)));
        System.out.println(JsonUtils.toJson(list)
            .replaceAll("\\*","")
            .replaceAll("\\n","")
            .replaceAll("\\t","")
            .replaceAll(" ","")
        );

        GetTypeClass(JsonUtils.toJson(list));

        System.out.println(ResultMsg(JsonUtils.toJson(user)));


    }

}
