package com.atguigu.ggkt.vod.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.annotations.Cacheable;

@Configuration
@MapperScan("com/atguigu/ggkt/vod/mapper")
public class VodConfig {
}
