package com.ssafy.tripgg.global.config;

import com.ssafy.tripgg.global.config.enum_converter.StringToOrderBy;
import com.ssafy.tripgg.global.config.enum_converter.StringToRegion;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration //spring security(SecurityConfig)에서 cors와 인증 인가 주고 있음
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.url.front}")
    private String frontUrl;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(frontUrl)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                .allowedHeaders("Authorization", "Content-Type", "X-Requested-With")
                .allowCredentials(true)
                .maxAge(3600);
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToRegion());
        registry.addConverter(new StringToOrderBy());
    }
}
