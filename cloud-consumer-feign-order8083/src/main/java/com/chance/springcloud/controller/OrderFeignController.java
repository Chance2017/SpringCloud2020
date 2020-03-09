package com.chance.springcloud.controller;

import com.chance.springcloud.entities.CommonResult;
import com.chance.springcloud.entities.Payment;
import com.chance.springcloud.service.PaymentFeignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Slf4j
public class OrderFeignController {
    @Resource
    private PaymentFeignService paymentFeignService;

    @GetMapping("/consumer/payment/get/{id}")
    public CommonResult<Payment> getPaymentById(@PathVariable("id") Long id) {
        return paymentFeignService.getPaymentById(id);
    }

    @GetMapping("/consumer/payment/feign/timeout")
    public String paymentFeignTimeout() {
        // 测试超时控制，OpenFeign默认只等待一秒钟，可在yml配置文件中配置ribbon超时时间
        return paymentFeignService.paymentFeignTimeout();
    }
}
