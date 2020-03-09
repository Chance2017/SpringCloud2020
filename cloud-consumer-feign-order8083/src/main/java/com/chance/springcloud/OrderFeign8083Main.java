package com.chance.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class OrderFeign8083Main {
    public static void main(String[] args) {
        SpringApplication.run(OrderFeign8083Main.class, args);
    }
}
