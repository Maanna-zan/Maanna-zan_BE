package com.hanghae99.maannazan.global.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class Swagger2Config {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("Maanna-zan_BE")
                .pathsToMatch("/**")
                .build();
    }
    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Maanna-zan API")   //info임포트 oas로 돼있음
                        .description("항해 13기 5조 Maanna-zan 프로젝트")
                        .version("v0.0.1"));
    }
}

