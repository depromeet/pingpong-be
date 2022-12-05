package com.dpm.winwin.api.configuration;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;


@Configuration
public class RestTemplateConfiguration {

    @Bean
    public RestTemplate restTemplate() {
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
        return restTemplateBuilder
                .setConnectTimeout(Duration.ofSeconds(5))
                .setReadTimeout(Duration.ofSeconds(5))
                .build();
    }
}
