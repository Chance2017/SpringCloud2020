package com.chance.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class PaymentEureka8002Main {
    public static void main(String[] args) {
        SpringApplication.run(PaymentEureka8002Main.class, args);
    }
}
