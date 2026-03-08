package com.example.demo.dto;

import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InboxMessageResponse {
    private Long id;
    private Long reviewId;
    private Long productId;
    private String title;
    private String message;
    private String createdByAdmin;
    private LocalDateTime createdAt;
    private boolean read;
}
