package com.dpm.winwin.chatting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@ComponentScan(basePackages = {
        "com.dpm.winwin.chatting",
        "com.dpm.winwin.domain"
})
@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.dpm.winwin.domain.repository")
@EntityScan("com.dpm.winwin.domain.entity")
@ComponentScan(basePackages = {
        "com.dpm.winwin.chatting",
        "com.dpm.winwin.domain"
})
public class WinwinChattingApplication {

  public static void main(String[] args) {
      SpringApplication.run(WinwinChattingApplication.class);
  }
}
