package com.example.demo.service;

import com.example.demo.dto.*;

import java.util.List;

public interface OrderService {
    OrderResponse placeOrder(String username, PlaceOrderRequest req);
    OrderResponse pay(String username, PayRequest req);

    String status(String username, Long orderId);

    List<OrderResponse> myOrders(String username);

    // internal (for payment-service later)
    InternalOrderDTO internalGet(Long orderId);
    void internalMarkPaid(Long orderId);
}