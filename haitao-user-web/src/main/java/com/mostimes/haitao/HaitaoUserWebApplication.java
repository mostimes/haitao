package com.mostimes.haitao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(basePackages = {"com.mostimes"})
@ComponentScan("com.mostimes")
public class HaitaoUserWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(HaitaoUserWebApplication.class, args);
    }

}
