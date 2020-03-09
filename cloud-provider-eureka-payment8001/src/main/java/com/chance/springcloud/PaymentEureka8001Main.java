package com.chance.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
@EnableDiscoveryClient
public class PaymentEureka8001Main {
    public static void main(String[] args) {
        SpringApplication.run(PaymentEureka8001Main.class, args);
    }
}
