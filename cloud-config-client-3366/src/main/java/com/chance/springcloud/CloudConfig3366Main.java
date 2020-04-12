package com.chance.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class CloudConfig3366Main {
    public static void main(String[] args) {
        SpringApplication.run(CloudConfig3366Main.class, args);
    }
}
