package com.example.demo.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RazorpayCreateResponse {
    private String keyId;
    private String razorpayOrderId;
    private Long orderId;
    private BigDecimal amount;   // in INR
    private Long amountPaise;    // in paise
    private String currency;
}