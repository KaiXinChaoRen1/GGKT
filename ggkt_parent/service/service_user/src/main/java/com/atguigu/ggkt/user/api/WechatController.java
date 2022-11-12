package com.atguigu.ggkt.user.api;

import com.alibaba.fastjson.JSON;
import com.atguigu.ggkt.model.user.UserInfo;
import com.atguigu.ggkt.user.service.UserInfoService;
import com.atguigu.ggkt.utils.JwtHelper;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;

/**
 * 微信用户进入公众号h5前端页面要进行后端授权再跳转前端
 */

@Controller         //用于页面跳转
@RequestMapping("/api/user/wechat")
public class WechatController {

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private WxMpService wxMpService;

    @Value("${wechat.userInfoUrl}")
    private String userInfoUrl;

    //授权跳转的主方法
    @GetMapping("authorize")
    public String authorize(@RequestParam("returnUrl") String returnUrl, HttpServletRequest request) {
        //调用官方工具包的方法
        String url = wxMpService.oauth2buildAuthorizationUrl(
                userInfoUrl,
                WxConsts.OAUTH2_SCOPE_USER_INFO,
                URLEncoder.encode(returnUrl.replace("guiguketan", "#"))
        );
        return "redirect:" + url;
    }

    //wxMpService工具包中要访问这个接口
    @GetMapping("userInfo")
    public String userInfo(@RequestParam("code") String code,
                           @RequestParam("state") String returnUrl) {
        try {
            //拿着code请求
            WxMpOAuth2AccessToken wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
            //获取openid
            String openId = wxMpOAuth2AccessToken.getOpenId();
            System.out.println("openid:" + openId);

            //获取微信信息
            WxMpUser wxMpUser = wxMpService.oauth2getUserInfo(wxMpOAuth2AccessToken, null);
            System.out.println("wxMpUser:" + JSON.toJSONString(wxMpUser));

            //获取微信信息添加数据库
            //根据openId查询数据库信息(没有就添加)
            UserInfo userInfo = userInfoService.getUserInfoOpenid(openId);
            if (userInfo == null) {
                userInfo = new UserInfo();
                userInfo.setOpenId(openId);
                userInfo.setNickName(wxMpUser.getNickname());
                userInfo.setAvatar(wxMpUser.getHeadImgUrl());
                userInfo.setSex(wxMpUser.getSexId());
                userInfo.setProvince(wxMpUser.getProvince());
                userInfoService.save(userInfo);
            }

            //授权完成之后，生成token携带跳转到具体功能页面
            String token = JwtHelper.createToken(userInfo.getId(), userInfo.getNickName());

            if (returnUrl.indexOf("?") == -1) {                         //如果路径中本来一个参数都没有
                return "redirect:" + returnUrl + "?token=" + token;     //添加参数
            } else {                                                    //否则
                return "redirect:" + returnUrl + "&token=" + token;     //拼接参数
            }

        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        return null;
    }

}
