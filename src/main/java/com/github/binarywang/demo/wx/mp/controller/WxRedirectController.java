package com.github.binarywang.demo.wx.mp.controller;

import com.github.binarywang.demo.wx.mp.config.WxMpConfiguration;
import com.github.binarywang.demo.wx.mp.config.WxMpProperties;
import com.github.binarywang.demo.wx.mp.result.GlobalEumn;
import com.github.binarywang.demo.wx.mp.utils.JsonUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.Action;

import static com.github.binarywang.demo.wx.mp.result.ResultMsg.ResultMsg;

/**
 * @author Edward
 */
@Slf4j
@AllArgsConstructor
@Controller
@RequestMapping("/wx/redirect/{appid}")
public class WxRedirectController {

    private final WxMpService wxService;

    @RequestMapping("/greet")
    public String greetUser(@PathVariable String appid, @RequestParam String code, ModelMap map) {

        if (!this.wxService.switchover(appid)) {
            throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", appid));
        }
        log.info("我是{} {} ",appid,code);
        try {
            WxMpOAuth2AccessToken accessToken = wxService.oauth2getAccessToken(code);
            WxMpUser user = wxService.oauth2getUserInfo(accessToken, null);
            log.info("我是{}  {}  {}",appid,code,user.toString());
            map.put("user", user);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }

        return "greet_user";
    }

    @RequestMapping("/greetbefore")
    public String greetbefore(@PathVariable String appid) {

        if (!this.wxService.switchover(appid)) {
            throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", appid));
        }
        log.info("我是{}",appid);
        String url = this.wxService.switchoverTo(appid).oauth2buildAuthorizationUrl(
            String.format("http://aaa.warelucent.cc/hlwxmp/wx/redirect/%s/greet", appid),
            WxConsts.OAuth2Scope.SNSAPI_USERINFO, null);
        log.info("我是url:{}",url);

        return "redirect:"+url;
    }

    @RequestMapping("/active")
    public String active(@PathVariable String appid, HttpServletRequest request) {

        String url = request.getScheme()+"://"+ request.getServerName()
            +(request.getServerPort()==80 ? "":":"+request.getServerPort())+request.getRequestURI();
        url=url+(request.getQueryString()== null ? "":"?"+request.getQueryString());
        System.out.println("获取全路径（协议类型：//域名/项目名/命名空间/action名称?其他参数）url="+url);
        String url2=request.getScheme()+"://"+ request.getServerName();//+request.getRequestURI();
        System.out.println("协议名：//域名="+url2);
        System.out.println("获取项目名="+request.getContextPath());
        System.out.println("获取参数="+request.getQueryString());
        System.out.println("获取全路径="+request.getRequestURL());

        if (!this.wxService.switchover(appid)) {
            throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", appid));
        }
        log.info("我是{}",appid);

        String url3 = this.wxService.switchoverTo(appid).oauth2buildAuthorizationUrl(
            String.format("http://aaa.warelucent.cc/hlwxmp/mapper/activate?opendId=%s", "89"),
            WxConsts.OAuth2Scope.SNSAPI_USERINFO, null);
        log.info("我是url:{}",url);

        return "redirect:"+url3;
    }


}
