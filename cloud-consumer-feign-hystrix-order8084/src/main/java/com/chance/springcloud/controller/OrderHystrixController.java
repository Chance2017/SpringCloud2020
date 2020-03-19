package com.chance.springcloud.controller;

import com.chance.springcloud.service.OrderHystrixService;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Slf4j
@DefaultProperties(defaultFallback = "orderGlobalFallbackMethod")   // 设置全局服务降级处理方法
public class OrderHystrixController {

    @Resource
    private OrderHystrixService service;

    @GetMapping("/consumer/payment/hystrix/ok/{id}")
    String paymentInfo_OK(@PathVariable("id") Integer id) {
        return service.paymentInfo_OK(id);
    }

    @GetMapping("/consumer/payment/hystrix/timeout/{id}")
//    @HystrixCommand(fallbackMethod = "paymentInfo_TimeoutHandler", commandProperties = {
//            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000")
//    })    // 使用定制的全局服务降级处理方法
    @HystrixCommand // 使用默认的全局服务降级处理方法
    String paymentInfo_Timeout(@PathVariable("id") Integer id) {
//        int age = 10 / 0; // 测试消费者端异常
        return service.paymentInfo_Timeout(id);
    }

    public String paymentInfo_TimeoutHandler(Integer id) {
        return "我是消费者8084，本身或提供者端系统繁忙或运行错误，请稍后再试, id: " + id;
    }

    // 下面是全局服务降级fallback处理方法
    public String orderGlobalFallbackMethod() {
        return "全局异常（服务降级）处理信息，请稍后再试";
    }
}






