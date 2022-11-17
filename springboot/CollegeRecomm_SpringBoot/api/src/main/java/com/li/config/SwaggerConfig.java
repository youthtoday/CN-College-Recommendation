package com.li.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    /*swagger会帮助我们生成接口文档
    * 1：配置文档信息
    * 2：配置生成规则
     */
    //Docket封装接口文档信息
    @Bean
    public Docket getDocket(){
        ApiInfoBuilder apiInfoBuilder = new ApiInfoBuilder();
        apiInfoBuilder.title("《大学推荐网站》后端接口文档")
                .description("此文档详细说明了大学推荐网站的后端接口规范")
                .version("v 1.0.1")
                .contact(new Contact("李疆梅","www.li.com","1985505294@qq.com"));
        ApiInfo apiInfo = apiInfoBuilder.build();
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo)//指定生成的文档封面信息：标题，作者
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.li.controller"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }
}
