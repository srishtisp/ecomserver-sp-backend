package com.example.demo.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.dto.OrderDto;

import java.util.List;

@FeignClient(name = "OrderService")
public interface OrderClient {

    @GetMapping("/orders/admin")
    List<OrderDto> getAllOrders();

}