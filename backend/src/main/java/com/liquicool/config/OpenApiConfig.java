package com.liquicool.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI liquicoolOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("立冷科技 LIQUICOOL API")
                        .description("立冷科技 LIQUICOOL 数据中心液冷解决方案门户与后台管理系统 API 文档")
                        .version("1.0.0"));
    }
}
