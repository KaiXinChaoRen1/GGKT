package com.atguigu.ggkt.client.user;

import com.atguigu.ggkt.model.user.UserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 *  编写接口专门提供远程调用
 *  跟据id获取用户信息,多个模块用到
 */

@FeignClient(value = "service-user") //接口所在的服务名字
public interface UserInfoFeignClient {

    @GetMapping("/admin/user/userInfo/inner/getById/{id}")  //路径要写全
    public UserInfo getById(@PathVariable Long id);
}
