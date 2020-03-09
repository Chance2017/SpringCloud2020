package com.chance.springcloud.controller;

import com.chance.springcloud.entities.CommonResult;
import com.chance.springcloud.entities.Payment;
import com.chance.springcloud.lb.LoadBalancer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.net.URI;
import java.util.List;

@RestController
@Slf4j
public class OrderController {

    //    public static final String PAYMENT_URL = "http://localhost:8001";
    // 使用eureka
    public static final String PAYMENT_URL = "http://CLOUD-PAYMENT-SERVICE";

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private LoadBalancer loadBalancer;

    @Resource
    private DiscoveryClient discoveryClient;

    @PostMapping("/consumer/payment/create")
    public CommonResult<Payment> create(@RequestBody Payment payment) {
        return restTemplate.postForObject(PAYMENT_URL + "/payment/create", payment, CommonResult.class);
    }

    @GetMapping("/consumer/payment/get/{id}")
    public CommonResult<Payment> getPayment(@PathVariable("id") Long id) {
        return restTemplate.getForObject(PAYMENT_URL + "/payment/get/" + id, CommonResult.class);
    }

    @GetMapping("/consumer/payment/get/entity/{id}")
    public CommonResult<Payment> getPaymentEntity(@PathVariable("id") Long id) {
        ResponseEntity<CommonResult> entity = restTemplate.getForEntity(PAYMENT_URL + "/payment/get/" + id, CommonResult.class);
        if (entity.getStatusCode().is2xxSuccessful()) {
            log.info(entity.getStatusCode() + "\t" + entity.getHeaders());
            return entity.getBody();
        } else {
            return new CommonResult<>(444, "请求失败");
        }
    }

    @GetMapping("/consumer/payment/lb")
    public String getPaymentLB() {
        List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
        if (instances == null || instances.isEmpty()) {
            return null;
        }
        ServiceInstance serviceInstance = loadBalancer.getInstance(instances);
        URI             uri             = serviceInstance.getUri();
        System.out.println(uri);
        return restTemplate.getForObject(uri + "/payment/lb", String.class);
    }

}
