package com.example.demo.dto;

import java.util.List;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponse {

    private Double averageRating;

    private List<ReviewDto> reviews;   // ✅ CORRECT
}