package com.github.binarywang.demo.wx.mp.mycontroller;


import com.github.binarywang.demo.wx.mp.config.ImageUrl;
import com.github.binarywang.demo.wx.mp.result.GlobalEumn;
import com.github.binarywang.demo.wx.mp.utils.DownloadPicFromURL;
import com.github.binarywang.demo.wx.mp.utils.JsonUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.card.WxMpCardResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static com.github.binarywang.demo.wx.mp.result.ResultMsg.ResultMsg;
import static com.github.binarywang.demo.wx.mp.utils.DeleteFileUtil.deleteFile;
import static com.github.binarywang.demo.wx.mp.utils.DownloadPicFromURL.downloadPicture;
import static com.github.binarywang.demo.wx.mp.utils.HttpUtils.postImageStream;

/**
 * Created by Administrator on 2019-05-29.
 */
@Slf4j
@AllArgsConstructor
@Controller
@RequestMapping("/membercard/{appid}")
public class memberCardConntroller {

    private final WxMpService wxService;

    private final ImageUrl image;
    /**
     *  卡卷核销接口
     * @return
     */
    @PostMapping("/card/code/consume")
    @ResponseBody
    public String consumeCode(@PathVariable String appid,@RequestParam(name = "cardId",required = true) String cardId,
                            @RequestParam(name = "code",required = true) String code) {
        try {

            if (!this.wxService.switchover(appid)) {
                String Msg=String.format("未找到对应appid=[%s]的配置，请核实！", appid);
                log.info(Msg);
                return ResultMsg(GlobalEumn.APPID_NOT_EXIT);
            }

            //先查询卡卷状态  判断卡卷是否满足核销资格
            WxMpCardResult wxMpCardResult = this.wxService.getCardService().queryCardCode(cardId, code, false);
            //开始核销卡卷
            if(wxMpCardResult.getErrorCode().equals("0") && wxMpCardResult.getUserCardStatus().equals("NORMAL")){
                String result = this.wxService.getCardService().consumeCardCode(code, cardId);
                JsonObject json = (new JsonParser()).parse(result).getAsJsonObject();
                if(json.has("errcode") && json.get("errcode").getAsInt()==0){
                    String openid=json.get("openid").toString();
                    return ResultMsg(result);
                }else{
                    return ResultMsg(GlobalEumn.CARD_CONSUME_FAIL);
                }
            }else{
                //不满足核销状态
                return ResultMsg(GlobalEumn.CARD_STUTAS_FAIL);
            }
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        return ResultMsg(GlobalEumn.CARD_CONSUME_FAIL);
    }

    /**
     *   查询卡卷
     * @param cardId
     * @param code
     * @return
     */
    @PostMapping("/card/code/get")
    @ResponseBody
    public String getCode(@PathVariable String appid,@RequestParam(name = "cardId",required = true) String cardId,
                            @RequestParam(name = "code",required = true) String code) {
        try {
            if (!this.wxService.switchover(appid)) {
                String Msg=String.format("未找到对应appid=[%s]的配置，请核实！", appid);
                log.info(Msg);
                return ResultMsg(GlobalEumn.APPID_NOT_EXIT);
            }

            WxMpCardResult wxMpCardResult = this.wxService.getCardService().queryCardCode(cardId, code, false);
            return ResultMsg(wxMpCardResult.toString());
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        return ResultMsg(GlobalEumn.CARD_CONSUME_FAIL);
    }

    /**
     *   创建卡卷
     */
    @PostMapping("/card/code/createByJson")
    @ResponseBody
    public String createCode(@PathVariable String appid,@RequestBody String json) {
        try {
            if (!this.wxService.switchover(appid)) {
                String Msg=String.format("未找到对应appid=[%s]的配置，请核实！", appid);
                log.info(Msg);
                return ResultMsg(GlobalEumn.APPID_NOT_EXIT);
            }
            String url = "https://api.weixin.qq.com/card/create";
            String result = this.wxService.post(url, json);
            return ResultMsg(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultMsg(GlobalEumn.CARD_CREATE_FAIL);
        }
    }

    /**
     *  上传图片
     * @param appid
     * @param imgUrl  网络路径
     * @return
     */
    @PostMapping("/card/media/uploadimg")
    @ResponseBody
    public String uploadImg(@PathVariable String appid,@RequestParam(name = "imgUrl",required = true) String imgUrl) {
        try {
            if (!this.wxService.switchover(appid)) {
                String Msg=String.format("未找到对应appid=[%s]的配置，请核实！", appid);
                log.info(Msg);
                return ResultMsg(GlobalEumn.APPID_NOT_EXIT);
            }
            deleteFile(image.getUrl());
            try{
                downloadPicture(imgUrl,image.getUrl());
            }catch (Exception e){
                e.printStackTrace();
                log.info("图片下载到本地失败 {} ",e.getMessage());
                return ResultMsg(GlobalEumn.IMAGE_DOWNLOAD_FAIL);
            }
            log.info(" image.getUrl() {}",image.getUrl());
            log.info(" 接收到的路径 {}",imgUrl);
            String url=String.format("https://api.weixin.qq.com/cgi-bin/media/uploadimg?access_token=%s",
                                     this.wxService.getAccessToken(false));
            log.info(" url {}",imgUrl);
            String result = postImageStream(url, image.getUrl());
            return ResultMsg(result);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("图片从本地上传到微信CDN失败 {} ",e.getMessage());
            return ResultMsg(GlobalEumn.IMAGE_UPLOADIMG_FAIL);
        }
    }


    @PostMapping("/card/code/decrypt")
    @ResponseBody
    public String codeDcrypt(@PathVariable String appid,@RequestParam(name = "encryptCode",required = true) String encryptCode) {
        try {
            if (!this.wxService.switchover(appid)) {
                String Msg=String.format("未找到对应appid=[%s]的配置，请核实！", appid);
                log.info(Msg);
                return ResultMsg(GlobalEumn.APPID_NOT_EXIT);
            }
            String cardCode = this.wxService.getCardService().decryptCardCode(encryptCode);
            return ResultMsg(cardCode);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultMsg(GlobalEumn.CARD_DECRYPT_FAIL);
        }
    }



}
