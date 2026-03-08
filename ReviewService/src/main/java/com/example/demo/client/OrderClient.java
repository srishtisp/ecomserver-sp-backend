package com.example.demo.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "OrderService")
public interface OrderClient {

    @GetMapping("/orders/internal/purchased")
    boolean hasPurchased(
            @RequestParam Long userId,
            @RequestParam Long productId);
}