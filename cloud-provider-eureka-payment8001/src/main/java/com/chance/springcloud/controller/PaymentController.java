package com.chance.springcloud.controller;

import com.chance.springcloud.entities.CommonResult;
import com.chance.springcloud.entities.Payment;
import com.chance.springcloud.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
public class PaymentController {
    @Resource
    private PaymentService paymentService;

    @Value("${server.port}")
    private String serverPort;

    @Resource
    private DiscoveryClient discoveryClient;

    @ PostMapping("/payment/create")
    public CommonResult create(@RequestBody Payment payment) {
        int result = paymentService.create(payment);
        log.info("payment 插入结果: {}", result);
        if (result > 0) {
            return new CommonResult(200, "插入数据库成功, 服务端口号：" + serverPort);
        } else {
            return new CommonResult(444, "插入数据库失败, 服务端口号：" + serverPort);
        }
    }

    @GetMapping("/payment/get/{id}")
    public CommonResult getPaymentById(@PathVariable("id") Long id) {
        Payment payment = paymentService.getPaymentById(id);
        log.info("Payment 查询结果: {}", payment);
        if (payment == null) {
            return new CommonResult(445, "未查询到 id=" + id + " 对应记录, 服务端口号：" + serverPort);
        } else {
            return new CommonResult(200, "OK, 服务端口号：" + serverPort, payment);
        }
    }

    @GetMapping("/payment/discovery")
    public Object discovery() {
        // 服务发现，获取共注册了哪些服务
        List<String> services = discoveryClient.getServices();
        for (String service : services) {
            log.info("service: {}", service);
        }

        // 获取某个服务id下都注册了哪些实例
        List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
        for (ServiceInstance instance : instances) {
            log.info("service instance id: {}, host: {}, port: {}, uri: {}", instance.getServiceId(), instance.getHost(), instance.getPort(), instance.getUri());
        }
        return this.discoveryClient;
    }

    @GetMapping("/payment/lb")
    public String getPaymentLB() {
        return serverPort;
    }

    @GetMapping("/payment/feign/timeout")
    public String paymentFeignTimeout() {
        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return serverPort;
    }
}
