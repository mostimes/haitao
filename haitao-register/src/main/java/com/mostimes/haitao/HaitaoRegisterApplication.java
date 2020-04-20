package com.mostimes.haitao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class HaitaoRegisterApplication {

    public static void main(String[] args) {
        SpringApplication.run(HaitaoRegisterApplication.class, args);
    }

}
