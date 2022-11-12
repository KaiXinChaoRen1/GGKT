package com.atguigu.ggkt.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * 这个user模块不是后台管理系统的管理员那个user
 * 而是真正使用这个公众号服务的用户user
 */

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.atguigu.ggkt.user.mapper")
@ComponentScan(basePackages = "com.atguigu")
public class ServiceUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceUserApplication.class, args);
    }

}
