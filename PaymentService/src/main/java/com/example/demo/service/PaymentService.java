package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.PayRequest;
import com.example.demo.dto.PaymentResponse;

public interface PaymentService {
    PaymentResponse pay(String username, PayRequest req);
    PaymentResponse status(String username, Long orderId);
    List<PaymentResponse> myPayments(String username);
    PaymentResponse retry(String username, Long paymentId);
    PaymentResponse refund(String username, Long orderId);
    List<PaymentResponse> getAllPayments();
}