package com.example.demo.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {

    private Long productId;
    private String productName;
    private String description;
    private BigDecimal price;

    private Long vendorId;
    private String vendorName;
    private String category;

    private boolean active;
    
    private Double averageRating;
    private Integer reviewCount;
    private List<ReviewDto> reviews;
}