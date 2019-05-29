package com.github.binarywang.demo.wx.mp.mycontroller;

import com.github.binarywang.demo.wx.mp.domain.User;
import com.github.binarywang.demo.wx.mp.result.GlobalEumn;
import com.github.binarywang.demo.wx.mp.utils.JsonUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxCardApiSignature;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static com.github.binarywang.demo.wx.mp.result.ResultMsg.ResultMsg;

/**
 * Created by Administrator on 2019-05-24.
 */
@Slf4j
@AllArgsConstructor
@Controller
@RequestMapping("/mapper")
public class myMapsend {

    private final WxMpService wxService;

    @RequestMapping("/activate")
    public String selectUser(@RequestParam String opendId, ModelMap map,HttpServletRequest request) {

        //获取所有参数
        Enumeration<String> e = request.getParameterNames();

        //拼接所有参数
        String data = "";
        Map<String,String> mapParams=new HashMap<String, String>();
        while (e.hasMoreElements()) {
            String paramName = (String) e.nextElement();
            String paramValue = request.getParameterValues(paramName)[0];
            data = data + paramName + "=" + paramValue + "&";
            mapParams.put(paramName,paramValue);
        }
        data=data.equals("")? "":data.substring(0, data.lastIndexOf("&"));
        log.info(String.format("所有的请求参数 %s",data));
        try {
            log.info(String.format("我是来自%s这里的请求",opendId));
            map.put("opendId", opendId);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return "activate";
    }

    @PostMapping("/getJsconfig")
    @ResponseBody
    public String greetTest(HttpServletRequest request,@RequestParam String signUrl) {

        String url = request.getScheme()+"://"+ request.getServerName()
            +(request.getServerPort()==80 ? "":":"+request.getServerPort())+request.getRequestURI()
            +(request.getQueryString()== null ? "":"?"+request.getQueryString());
        System.out.println("url ="+url);
        String url2=request.getScheme()+"://"+ request.getServerName();//+request.getRequestURI();
        System.out.println("协议名：//域名="+url2);
        System.out.println("获取项目名="+request.getContextPath());
        System.out.println("获取参数="+request.getQueryString());
        System.out.println("获取全路径="+request.getRequestURL());


        try {
            String JsapiTicket=this.wxService.getJsapiTicket();
            log.info("我是JsapiTicket   {}",JsapiTicket);
            //"http://aaa.warelucent.cc/hlwxmp/mapper/activate?opendId=89"
            WxJsapiSignature wxJsapiSignature=this.wxService.createJsapiSignature(signUrl);
            log.info("我是wxJsapiSignature{}",wxJsapiSignature);
            return ResultMsg(JsonUtils.toJson(wxJsapiSignature));
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        return ResultMsg(GlobalEumn.JSSDK_SIGN_FAIL);
    }


    @PostMapping("/getJsCardconfig")
    @ResponseBody
    public String getJsCardconfig(HttpServletRequest request,@RequestParam String card_id) {

        try {
            String JsapiCardTicket=this.wxService.getCardService().getCardApiTicket();
            log.info("我是JsapiTicket   {}",JsapiCardTicket);

            //app_id, card_id, card_type, code, openid, location_id
            String[] param = {"wx751d1e03d0988cb3",card_id};
            WxCardApiSignature cardApiSignature = this.wxService.getCardService().createCardApiSignature(param);
            log.info("cardApiSignature{}",cardApiSignature);
            return ResultMsg(JsonUtils.toJson(cardApiSignature));
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        return ResultMsg(GlobalEumn.JSSDK_SIGN_FAIL);
    }


    @PostMapping("/bindtest")
    @ResponseBody
    public String bindtest(@RequestParam String opendId) {
        List<User> list=new ArrayList<>();
        try {
            log.info(String.format("我是来自%s这里的请求",opendId));
            User user=null;
            user=new User("张明阳","http://12345799//44448975","男","中国");
            list.add(user);
            user=new User("张明阳2","http://12345799//44448975","男","中国");
            list.add(user);
            user=new User("张明阳3","http://12345799//44448975","男","中国");
            list.add(user);
            log.info(String.format("返回的结果集是%s",ResultMsg(JsonUtils.toJson(list))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultMsg(JsonUtils.toJson(list));
    }
}
