package com.chance.springcloud.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ApplicationContextConfig {
    @Bean
//    @LoadBalanced   // 赋予RestTemplate负载均衡的能力，使用此注解后，restTemplate请求会被ribbon拦截，当使用ip地址访问时就无效了，ribbon必须使用应用才能访问到
    public RestTemplate getTemplate() {
        return new RestTemplate();
    }
}
