package com.example.demo.dto;

import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDto {
    private Long reviewId;
    private Long productId;
    private String username;
    private int rating;
    private String comment;
    private LocalDateTime createdAt;
}
