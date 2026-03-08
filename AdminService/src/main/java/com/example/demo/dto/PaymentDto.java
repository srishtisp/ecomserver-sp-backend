package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDto {

    private Long paymentId;
    private Long orderId;
    private Long userId;
    private BigDecimal amount;
    private String mode;
    private String status;
    private LocalDateTime paymentDate;
    private String referenceId;

}
