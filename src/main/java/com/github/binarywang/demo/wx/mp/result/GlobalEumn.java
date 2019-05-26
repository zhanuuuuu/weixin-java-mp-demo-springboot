package com.github.binarywang.demo.wx.mp.result;

/**
 * Created by Administrator on 2019-05-24.
 */
public enum  GlobalEumn implements GlobalInfo {

    SUCCESS(1001, "成功"),
    JSSDK_SIGN_FAIL(1007, "JS-SDK签名失败"),
    JSON_ANALYZING_FAIL(1008, "服务端json返回时解析失败");

    private int code;

    private String message;



    GlobalEumn(int code, String message) {
        this.code=code;
        this.message=message;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getMesssage() {
        return this.message;
    }
}
