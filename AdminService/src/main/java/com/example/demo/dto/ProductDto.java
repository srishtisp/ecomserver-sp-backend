package com.example.demo.dto;

import lombok.Data;

@Data
public class ProductDto {

    private Long productId;
    private String productName;
    private String description;
    private double price;
    private Long vendorId;
    private boolean active;
}