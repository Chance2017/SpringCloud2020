package com.chance.springcloud.service;

import org.springframework.stereotype.Service;

@Service
public class OrderFallbackService implements OrderHystrixService {
    @Override
    public String paymentInfo_OK(Integer id) {
        return "OrderFallbackService paymentInfo_OK() fall back";
    }

    @Override
    public String paymentInfo_Timeout(Integer id) {
        return "OrderFallbackService paymentInfo_Timeout() fall back";
    }
}
