package com.github.binarywang.demo.wx.mp.result;

/**
 * Created by Administrator on 2019-05-24.
 */
public enum  GlobalEumn implements GlobalInfo {

    SUCCESS(1001, "成功"),
    CARD_DECRYPT_FAIL(1002, "优惠卷解密失败"),
    APPID_NOT_EXIT(1003, "该商户号不存在"),
    CARD_CREATE_FAIL(1004, "优惠卷创建失败"),
    CARD_STUTAS_FAIL(1005, "卡卷不满足核销状态"),
    CARD_CONSUME_FAIL(1006, "卡卷核销失败"),
    JSSDK_SIGN_FAIL(1007, "JS-SDK签名失败"),
    JSON_ANALYZING_FAIL(1008, "服务端json返回时解析失败"),
    IMAGE_UPLOADIMG_FAIL(1009, "图片上传微信服务端失败"),
    IMAGE_DOWNLOAD_FAIL(1010, "图片下载到本地失败");

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
