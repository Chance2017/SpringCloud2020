package com.chance.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient  // 该注解用于向使用consul或者zookeeper作为注册中心时注册服务
public class PaymentZK8004Main {
    public static void main(String[] args) {
        SpringApplication.run(PaymentZK8004Main.class, args);
    }
}
