package com.mostimes.haitao;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@MapperScan("com.mostimes.haitao.product.mapper")
@EnableEurekaClient//注册进eureka
@EnableCircuitBreaker//对hystrix熔断机制的支持
public class HaitaoProductServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(HaitaoProductServiceApplication.class, args);
    }

}
