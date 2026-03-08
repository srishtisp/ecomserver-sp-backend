package com.example.demo.dto;

import lombok.*;
import java.time.LocalDateTime;

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