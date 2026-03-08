package com.example.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.CreateReviewRequest;
import com.example.demo.dto.InboxMessageResponse;
import com.example.demo.dto.ProductReviewResponse;
import com.example.demo.dto.RejectReviewRequest;
import com.example.demo.dto.ReviewResponse;
import com.example.demo.entity.Review;
import com.example.demo.service.ReviewService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<?> createReview(
            @Valid @RequestBody CreateReviewRequest request,
            Authentication auth) {

        String username = auth.getName();

        reviewService.createReview(request, username);

        return ResponseEntity.ok("Review added");
    }

    @GetMapping("/product/{productId}")
    public ProductReviewResponse getProductReviews(
            @PathVariable Long productId) {

        return reviewService.getReviewsForProduct(productId);
    }
    
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> deleteOwnReview(@PathVariable Long reviewId, Authentication auth) {
        reviewService.deleteOwnReview(reviewId, auth.getName());
        return ResponseEntity.ok("Review deleted");
    }

    @DeleteMapping("/admin/{reviewId}")
    public ResponseEntity<?> rejectReviewAsAdmin(
            @PathVariable Long reviewId,
            @Valid @RequestBody RejectReviewRequest request,
            Authentication auth) {

        reviewService.rejectReviewAsAdmin(reviewId, request.getReason(), auth.getName());
        return ResponseEntity.ok("Review rejected by admin");
    }

    @GetMapping("/inbox/my")
    public List<InboxMessageResponse> myInbox(Authentication auth) {
        return reviewService.getMyInbox(auth.getName());
    }
    
    @GetMapping("/admin")
    public List<ReviewResponse> listReviewsForAdmin() {
        return reviewService.getAllReviews();
    }


}