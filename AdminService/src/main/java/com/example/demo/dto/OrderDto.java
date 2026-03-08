package com.example.demo.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class OrderDto {

    private Long orderId;
    private Long userId;
    private Long productId;
    private Integer quantity;
    private BigDecimal orderValue;
    private String orderStatus;
    private boolean paid;

}
