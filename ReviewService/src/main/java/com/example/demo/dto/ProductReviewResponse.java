package com.example.demo.dto;

import java.util.List;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductReviewResponse {

    private Double averageRating;

    private List<ReviewResponse> reviews;
}
