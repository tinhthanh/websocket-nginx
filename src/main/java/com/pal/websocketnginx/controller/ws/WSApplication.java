package com.pal.websocketnginx.controller.ws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.DispatcherServlet;

@EnableScheduling
@EnableAsync
@SpringBootApplication
public class WSApplication {

    public static void main(String[] args) {
        SpringApplication.run(WSApplication.class, args);
    }

    @Bean
    public DispatcherServlet dispatcherServlet() {
        return new CustomDispatcherServlet();
    }
}