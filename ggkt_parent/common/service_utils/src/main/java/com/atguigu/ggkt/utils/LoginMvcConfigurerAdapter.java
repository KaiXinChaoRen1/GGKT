package com.atguigu.ggkt.utils;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.Resource;

/**
 * 设置拦截器的拦截路径
 *
 * 拦截器可以调用IOC容器中的各种依赖，而过滤器不能
 * 过滤器可以修改request，而拦截器不能(?修改request是个什么概念)
 * 过滤器必须在servlet容器中实现，而拦截器可以适用于javaEE，javaSE等各种环境
 */

@Configuration
public class LoginMvcConfigurerAdapter extends WebMvcConfigurerAdapter {

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //添加自定义拦截器，设置路径
        registry.addInterceptor(new UserLoginInterceptor(redisTemplate)).addPathPatterns("/api/**");
        registry.addInterceptor(new AdminLoginInterceptor(redisTemplate)).addPathPatterns("/admin/**");
        super.addInterceptors(registry);
    }
}
