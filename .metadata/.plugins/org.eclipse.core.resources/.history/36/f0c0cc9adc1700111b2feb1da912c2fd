package com.example.demo.impl;

import com.example.demo.client.ProductClient;
import com.example.demo.client.SecurityClient;
import com.example.demo.dto.*;
import com.example.demo.entity.Order;
import com.example.demo.entity.OrderStatus;
import com.example.demo.repo.OrderRepo;
import com.example.demo.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepo orderRepo;
    private final SecurityClient securityClient;
    private final ProductClient productClient;

    private Long resolveUserId(String username) {
        var user = securityClient.getUser(username);
        if (user == null || user.userId == null) throw new RuntimeException("User not found: " + username);
        return user.userId;
    }

    private OrderResponse toResponse(Order o) {
        return OrderResponse.builder()
                .orderId(o.getOrderId())
                .userId(o.getUserId())
                .productId(o.getProductId())
                .quantity(o.getQuantity())
                .orderValue(o.getOrderValue())
                .orderStatus(o.getOrderStatus())
                .paid(o.isPaid())
                .orderDate(o.getOrderDate())
                .build();
    }

    @Override
    public OrderResponse placeOrder(String username, PlaceOrderRequest req) {

        Long userId = resolveUserId(username);

        var product = productClient.getInternal(req.getProductId());
        if (product == null) throw new RuntimeException("Product not found: " + req.getProductId());
        if (!product.active) throw new RuntimeException("Product is not active: " + req.getProductId());

        BigDecimal qty = BigDecimal.valueOf(req.getQuantity());
        BigDecimal total = product.price.multiply(qty);

        Order order = Order.builder()
                .userId(userId)
                .productId(req.getProductId())
                .quantity(req.getQuantity())
                .orderValue(total)
                .orderStatus(OrderStatus.PLACED)
                .paid(false)
                .orderDate(LocalDateTime.now())
                .build();

        return toResponse(orderRepo.save(order));
    }

    @Override
    public OrderResponse pay(String username, PayRequest req) {
        Long userId = resolveUserId(username);

        Order order = orderRepo.findById(req.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found: " + req.getOrderId()));

        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException("This order does not belong to you");
        }
        if (order.isPaid()) {
            throw new RuntimeException("Order already paid");
        }
        if (req.getAmount().compareTo(order.getOrderValue()) != 0) {
            throw new RuntimeException("Amount mismatch. Expected: " + order.getOrderValue());
        }

        // dummy success payment
        order.setPaid(true);
        return toResponse(orderRepo.save(order));
    }

    @Override
    @Transactional(readOnly = true)
    public String status(String username, Long orderId) {
        Long userId = resolveUserId(username);

        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException("This order does not belong to you");
        }

        return order.getOrderStatus().name();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> myOrders(String username) {
        Long userId = resolveUserId(username);
        return orderRepo.findByUserId(userId).stream().map(this::toResponse).toList();
    }

    // -------- internal for payment-service later --------

    @Override
    @Transactional(readOnly = true)
    public InternalOrderDTO internalGet(Long orderId) {
        Order o = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
        return InternalOrderDTO.builder()
                .orderId(o.getOrderId())
                .userId(o.getUserId())
                .orderValue(o.getOrderValue())
                .paid(o.isPaid())
                .orderStatus(o.getOrderStatus())
                .build();
    }

    @Override
    public void internalMarkPaid(Long orderId) {
        Order o = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
        o.setPaid(true);
        orderRepo.save(o);
    }
}