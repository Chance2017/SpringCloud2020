package com.chance.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class OrderZK8081Main {
    public static void main(String[] args) {
        SpringApplication.run(OrderZK8081Main.class, args);
    }
}
