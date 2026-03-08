package com.example.demo.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateReviewRequest {

    private Long productId;

    @Min(1)
    @Max(5)
    private int rating;

    private String comment;
}