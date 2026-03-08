package com.example.demo.dto;

import java.time.LocalDateTime;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponse {

	private Long reviewId;
    private Long productId;
    private String username;
    private int rating;
    private String comment;
    private LocalDateTime createdAt;
}