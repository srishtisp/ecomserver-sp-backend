package com.example.demo.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.ReviewDto;
import com.example.demo.dto.ReviewResponse;

import java.util.List;

@FeignClient(name="ReviewService")
public interface ReviewClient {

    @GetMapping("/reviews/product/{productId}")
    ReviewResponse getReviews(@PathVariable Long productId);
}