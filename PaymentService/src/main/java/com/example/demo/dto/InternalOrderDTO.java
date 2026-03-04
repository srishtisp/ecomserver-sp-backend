package com.example.demo.dto;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InternalOrderDTO {

    private Long orderId;
    private Long userId;
    private BigDecimal orderValue;
    private boolean paid;
}