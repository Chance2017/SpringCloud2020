package com.chance.springcloud;

import com.chance.balance.MyselfRule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;

@SpringBootApplication
@EnableEurekaClient
@RibbonClient(name="CLOUD-PAYMENT-SERVICE", configuration= MyselfRule.class)
public class OrderEureka8080Main {

    public static void main(String[] args) {
        SpringApplication.run(OrderEureka8080Main.class, args);
    }

}
