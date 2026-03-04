package com.example.demo.controller;

import com.example.demo.dto.PayRequest;
import com.example.demo.dto.PaymentResponse;
import com.example.demo.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    // customer story: /pay
    @PostMapping("/pay")
    public PaymentResponse pay(Authentication auth, @Valid @RequestBody PayRequest req) {
        return paymentService.pay(auth.getName(), req);
    }

    // customer story: /status
    @GetMapping("/status")
    public PaymentResponse status(Authentication auth, @RequestParam Long orderId) {
        return paymentService.status(auth.getName(), orderId);
    }
    @GetMapping("/my")
    public List<PaymentResponse> myPayments(Authentication auth) {
        return paymentService.myPayments(auth.getName());
    }
    @PostMapping("/retry/{paymentId}")
    public PaymentResponse retry(Authentication auth,
                                 @PathVariable Long paymentId) {
        return paymentService.retry(auth.getName(), paymentId);
    }
    @PostMapping("/refund/{orderId}")
    public PaymentResponse refund(Authentication auth,
                                  @PathVariable Long orderId) {
        return paymentService.refund(auth.getName(), orderId);
    }
}