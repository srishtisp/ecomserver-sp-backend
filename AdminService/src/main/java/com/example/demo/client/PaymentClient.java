package com.example.demo.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.example.demo.dto.PaymentDto;

@FeignClient(name = "PaymentService")
public interface PaymentClient {

    @GetMapping("/payments/admin")
    List<PaymentDto> getAllPayments();

}