package com.chance.springcloud.service;

import com.chance.springcloud.entities.Payment;

public interface PaymentService {
    public int create(Payment payment);
    public Payment getPaymentById(Long id);
}
