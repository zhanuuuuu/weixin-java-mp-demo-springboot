package com.github.binarywang.demo.wx.mp.handler;

import com.github.binarywang.demo.wx.mp.utils.JsonUtils;

import com.github.binarywang.demo.wx.mp.utils.JsonUtils;
import com.google.gson.JsonObject;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.Map;



@Component
public class MemberCardHandler extends AbstractHandler {





    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> map,
                                    WxMpService wxMpService, WxSessionManager wxSessionManager) throws WxErrorException {

        this.logger.info("接收到请求消息，内容：{}", JsonUtils.toJson(wxMessage));

        String userCode = wxMessage.getUserCardCode();
        String cardId = wxMessage.getCardId();
        String openId = wxMessage.getOpenId();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("card_id", cardId);
        jsonObject.addProperty("code", userCode);



       String result= wxMpService.post(
                 String.format("https://api.weixin.qq.com/card/membercard/userinfo/get?access_token=%s",wxMpService.getAccessToken()),
           jsonObject.toString());

        this.logger.info("解析出来的会员卡内容：{}", result);



        return null;
    }
}


