package com.example.demo.repo;

import com.example.demo.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepo extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);
    boolean existsByUserIdAndProductIdAndPaidTrue(Long userId, Long productId);
}