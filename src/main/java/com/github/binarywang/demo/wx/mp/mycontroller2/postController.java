package com.github.binarywang.demo.wx.mp.mycontroller2;

import com.github.binarywang.demo.wx.mp.domain.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.kefu.WxMpKefuMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2019-05-23.
 */
@Slf4j
@AllArgsConstructor
@Controller
@RequestMapping("/post")
public class postController {

    private final WxMpService wxService;

    @RequestMapping("/getTocken")
    @ResponseBody
    public String getTocken() {

        String token="";
        try {
            token=wxService.getAccessToken();
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        log.info(String.format("Tocken:  %s",token));

        return String.format("Tocken:  %s",token);
    }
    @RequestMapping("/test/{appid}")
    @ResponseBody
    public String test(@PathVariable String appid , HttpServletRequest request) {

        String url = request.getScheme()+"://"+ request.getServerName()
            +(request.getServerPort()==80 ? "":":"+request.getServerPort())+request.getRequestURI();
        url=url+(request.getQueryString()== null ? "":"?"+request.getQueryString());
        System.out.println("获取全路径（协议类型：//域名/项目名/命名空间/action名称?其他参数）url="+url);
        String url2=request.getScheme()+"://"+ request.getServerName();//+request.getRequestURI();
        System.out.println("协议名：//域名="+url2);


        System.out.println("获取项目名="+request.getContextPath());
        System.out.println("获取参数="+request.getQueryString());
        System.out.println("获取全路径="+request.getRequestURL());

        log.info(String.format("我是来自%s这里的请求",url2));

        return String.format("我是来自%s这里的请求",url2);
    }

    @RequestMapping("/user/selectUser")
    public String selectUser( @RequestParam String currentPage, ModelMap map) {

        try {
            log.info(String.format("我是来自%s这里的请求",currentPage));
            map.put("user", currentPage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "user";
    }
    @RequestMapping("/greet/{appid}")
    public String greetUser(@PathVariable String appid, @RequestParam String code, ModelMap map) {

        log.info(String.format("我是来自%s这里的请求,我携带的参数是%s",appid,code));
        try {
            User user=new User();
            user.setCity("武汉");
            user.setHeadImgUrl(code);
            user.setNickname("ZMY "+appid);
            user.setSexDesc("男");
            map.put("user", user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "myuser";
    }

    /**
     *
     * @param appid
     * @return
     */
    @RequestMapping("/sendMessage/{appid}")
    @ResponseBody
    public String sendMessage(@PathVariable String appid) {

        log.info(String.format("我是来自%s这里的请求",appid));
        try {

            WxMpKefuMessage.WxArticle article1 = new WxMpKefuMessage.WxArticle();
            article1.setUrl("http://aaa.warelucent.cc/mpwx/GetWX_CODE?action=5");
            article1.setPicUrl("http://aaa.warelucent.cc/mpwx/pages/images/baijin.png");
            article1.setDescription("北京华隆购物超市");
            article1.setTitle("感谢光临");

            WxMpKefuMessage message=WxMpKefuMessage.NEWS()
                .toUser(appid)
                .addArticle(article1)
                .build();

            if(this.wxService.getKefuService().sendKefuMessage(message)){
                return "success";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
        return "fail";
    }
}
