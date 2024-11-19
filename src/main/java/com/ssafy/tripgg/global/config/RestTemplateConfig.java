package com.ssafy.tripgg.global.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Configuration
@Slf4j
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        // 로깅 인터셉터 추가
        restTemplate.setInterceptors(
                List.of((request, body, execution) -> {
                    log.info("Making request to: {} {}", request.getMethod(), request.getURI());
                    log.info("Headers: {}", request.getHeaders());

                    ClientHttpResponse response = execution.execute(request, body);

                    log.info("Received response: {}", response.getStatusCode());
                    return response;
                })
        );

        return restTemplate;
    }
}
