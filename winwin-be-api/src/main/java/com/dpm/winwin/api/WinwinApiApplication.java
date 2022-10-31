package com.dpm.winwin.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.dpm.winwin.domain.repository")
@EntityScan("com.dpm.winwin.domain.entity")
@ComponentScan(basePackages = {
    "com.dpm.winwin.api",
    "com.dpm.winwin.domain"
})
public class WinwinApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(WinwinApiApplication.class);
    }
}
