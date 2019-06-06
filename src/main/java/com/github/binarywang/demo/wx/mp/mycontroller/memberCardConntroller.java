package com.github.binarywang.demo.wx.mp.mycontroller;


import com.github.binarywang.demo.wx.mp.config.ImageUrl;
import com.github.binarywang.demo.wx.mp.result.GlobalEumn;
import com.github.binarywang.demo.wx.mp.utils.DownloadPicFromURL;
import com.github.binarywang.demo.wx.mp.utils.JsonUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.swagger.annotations.*;
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
@Api(value = "API 微信公众号卡卷对接", description = "微信公众号优惠卷接口梳理文档")
@Slf4j
@AllArgsConstructor
@Controller
@RequestMapping("/membercard/{appid}")
public class memberCardConntroller {

    private final WxMpService wxService;

    private final ImageUrl image;

    @ApiOperation(value="核销优惠卷", notes="根据cardId和code核销优惠卷")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "cardId", value = "优惠卷类别cardId",paramType ="query" ,required = true,dataType = "string"),
        @ApiImplicitParam(name = "code", value = "类别下的单张卡的code码",paramType ="query" ,required = true,dataType = "string")
    })
    @ApiResponses({
        @ApiResponse(code = 200, message = "Successful — 请求已完成",reference="77777",responseContainer="8888888"),
        @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
        @ApiResponse(code = 403, message = "服务器拒绝请求"),
        @ApiResponse(code = 401, message = "未授权客户机访问数据"),
        @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
        @ApiResponse(code = 500, message = "服务器不能完成请求")})
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

    @ApiOperation(value="优惠卷信息查询", notes="这个接口不需要其实,因为我的核销接口已经做了查询状态的判断")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "cardId", value = "优惠卷类别cardId",paramType ="query" ,required = true,dataType = "string",defaultValue = ""),
        @ApiImplicitParam(name = "code", value = "类别下的单张卡的code码",paramType ="query" ,required = true,dataType = "string",defaultValue = "")
    })
    @ApiResponses({
        @ApiResponse(code = 200, message = "Successful — 请求已完成",reference="77777",responseContainer="8888888"),
        @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
        @ApiResponse(code = 403, message = "服务器拒绝请求"),
        @ApiResponse(code = 401, message = "未授权客户机访问数据"),
        @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
        @ApiResponse(code = 500, message = "服务器不能完成请求")})
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

    @ApiOperation(value="创建优惠卷JSON格式", notes="创建优惠卷JSON格式")

    @ApiResponses({
        @ApiResponse(code = 200, message = "Successful — 请求已完成",reference="77777",responseContainer="8888888"),
        @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
        @ApiResponse(code = 403, message = "服务器拒绝请求"),
        @ApiResponse(code = 401, message = "未授权客户机访问数据"),
        @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
        @ApiResponse(code = 500, message = "服务器不能完成请求")})
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

    @ApiOperation(value="上传图片到微信服务端", notes="根据图片链接地址上传图片到微信服务端")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "imgUrl", value = "图片的链接",paramType ="query" ,required = true,dataType = "string")
    })
    @ApiResponses({
        @ApiResponse(code = 200, message = "Successful — 请求已完成",reference="77777",responseContainer="8888888"),
        @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
        @ApiResponse(code = 403, message = "服务器拒绝请求"),
        @ApiResponse(code = 401, message = "未授权客户机访问数据"),
        @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
        @ApiResponse(code = 500, message = "服务器不能完成请求")})
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

    @ApiOperation(value="解密卡卷的encryptCode", notes="解密卡卷的encryptCode获真是的code码")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "encryptCode", value = "卡号加密encryptCode码",paramType ="query" ,required = true,dataType = "string")
    })
    @ApiResponses({
        @ApiResponse(code = 200, message = "Successful — 请求已完成",reference="77777",responseContainer="8888888"),
        @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
        @ApiResponse(code = 403, message = "服务器拒绝请求"),
        @ApiResponse(code = 401, message = "未授权客户机访问数据"),
        @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
        @ApiResponse(code = 500, message = "服务器不能完成请求")})
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
