package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // ✅ customer story: /placeOrder
    @PostMapping("/placeOrder")
    public OrderResponse placeOrder(Authentication auth, @Valid @RequestBody PlaceOrderRequest req) {
        return orderService.placeOrder(auth.getName(), req);
    }

    // ✅ customer story: /pay (dummy here; later payment-service will do it)
    @PostMapping("/pay")
    public OrderResponse pay(Authentication auth, @Valid @RequestBody PayRequest req) {
        return orderService.pay(auth.getName(), req);
    }

    // ✅ customer story: /status (Placed/Shipped/Delivered)
    @GetMapping("/status")
    public String status(Authentication auth, @RequestParam Long orderId) {
        return orderService.status(auth.getName(), orderId);
    }

    // useful API
    @GetMapping("/my")
    public List<OrderResponse> myOrders(Authentication auth) {
        return orderService.myOrders(auth.getName());
    }

    // -------- internal endpoints for payment-service later --------
    @GetMapping("/internal/{orderId}")
    public InternalOrderDTO internal(@PathVariable Long orderId) {
        return orderService.internalGet(orderId);
    }

    @PutMapping("/{orderId}/internal/markPaid")
    public String internalMarkPaid(@PathVariable Long orderId) {
        orderService.internalMarkPaid(orderId);
        return "OK";
    }
}