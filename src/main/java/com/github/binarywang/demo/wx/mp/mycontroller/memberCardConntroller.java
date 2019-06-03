package com.github.binarywang.demo.wx.mp.mycontroller;

import com.github.binarywang.demo.wx.mp.result.GlobalEumn;
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

/**
 * Created by Administrator on 2019-05-29.
 */
@Slf4j
@AllArgsConstructor
@Controller
@RequestMapping("/membercard/{appid}")
public class memberCardConntroller {

    private final WxMpService wxService;


    /**
     *  卡卷核销接口
     * @return
     */
    @PostMapping("/card/code/consume")
    @ResponseBody
    public String greetTest(@RequestParam(name = "cardId",required = true) String cardId,
                            @RequestParam(name = "code",required = true) String code) {
        try {
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
}
