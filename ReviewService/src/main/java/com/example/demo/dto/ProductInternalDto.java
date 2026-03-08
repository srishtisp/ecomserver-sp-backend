package com.example.demo.dto;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductInternalDto {

    private Long productId;
    private String productName;
    private BigDecimal price;
    private boolean active;
}
