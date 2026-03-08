package com.example.demo.client;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.demo.dto.RejectReviewRequest;
import com.example.demo.dto.ReviewDto;

@FeignClient(name = "ReviewService")
public interface ReviewClient {

    @GetMapping("/reviews/admin")
    List<ReviewDto> getAllReviewsForAdmin();
    
    @DeleteMapping("/reviews/admin/{reviewId}")
    String rejectReview(@PathVariable Long reviewId, @RequestBody RejectReviewRequest request);
}
