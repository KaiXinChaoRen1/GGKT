package com.atguigu.ggkt.swagger;

import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 想要在别的模块中使用,需要将本模块引入要用的模块
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {
    @Bean
    public Docket webApiConfig(){
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("ggkt组")
                .apiInfo(webApiInfo())
                .select()
                //只显示api路径下的页面(注掉不写就代表全部显示)
                //.paths(Predicates.and(PathSelectors.regex("/api/.*")))
                .build();
    }

    private ApiInfo webApiInfo(){
        return new ApiInfoBuilder()
                .title("<我是标题>")
                .description("我是描述..............")
                .version("我是版本-1.0")
                .contact(new Contact("天才李文强", "http://hehe.com", "hehe@hehe.com"))
                .build();
    }
}
