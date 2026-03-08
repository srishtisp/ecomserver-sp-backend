package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.client.AuthClient;
import com.example.demo.client.OrderClient;
import com.example.demo.client.ProductClient;
import com.example.demo.dto.CreateReviewRequest;
import com.example.demo.dto.InboxMessageResponse;
import com.example.demo.dto.ProductReviewResponse;
import com.example.demo.dto.ReviewResponse;
import com.example.demo.entity.InboxMessage;
import com.example.demo.entity.Review;
import com.example.demo.repo.InboxMessageRepo;
import com.example.demo.repo.ReviewRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepo reviewRepo;
    private final AuthClient authClient;
    private final ProductClient productClient;
    private final OrderClient orderClient;
    private final InboxMessageRepo inboxMessageRepo;
    
    public void createReview(CreateReviewRequest request, String username) {

        // 1️⃣ verify product exists
        var product = productClient.getProduct(request.getProductId());

        if (product == null || !product.isActive()) {
            throw new RuntimeException("Product not available for review");
        }

        // 2️⃣ resolve user
        var user = authClient.getUser(username);

        // 3️⃣ verify user purchased the product
        boolean purchased = orderClient.hasPurchased(
                user.userId,
                request.getProductId()
        );

        if (!purchased) {
            throw new RuntimeException("You must purchase the product before reviewing it");
        }

        // 4️⃣ prevent duplicate reviews
        if (reviewRepo.existsByProductIdAndCustomerId(
                request.getProductId(), user.userId)) {

            throw new RuntimeException("You already reviewed this product");
        }

        // 5️⃣ create review
        Review review = Review.builder()
                .productId(request.getProductId())
                .customerId(user.userId)
                .customerName(user.username)
                .rating(request.getRating())
                .comment(request.getComment())
                .createdAt(LocalDateTime.now())
                .build();

        reviewRepo.save(review);
    }
    
    private ReviewResponse mapToResponse(Review r) {
        return ReviewResponse.builder()
                .reviewId(r.getReviewId())
                .productId(r.getProductId())
                .username(r.getCustomerName())
                .rating(r.getRating())
                .comment(r.getComment())
                .createdAt(r.getCreatedAt())
                .build();
    }

    public ProductReviewResponse getReviewsForProduct(Long productId) {

        List<Review> reviews = reviewRepo.findByProductId(productId);

        List<ReviewResponse> responseList = reviews.stream()
                .map(this::mapToResponse)
                .toList();

        double avg = reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0);

        return ProductReviewResponse.builder()
                .averageRating(avg)
                .reviews(responseList)
                .build();
    }
    
    public void deleteOwnReview(Long reviewId, String username) {
        var user = authClient.getUser(username);

        boolean isOwner = reviewRepo.existsByReviewIdAndCustomerId(reviewId, user.userId);
        if (!isOwner) {
            throw new RuntimeException("You can delete only your own review");
        }

        reviewRepo.deleteById(reviewId);
    }

    public void deleteReviewAsAdmin(Long reviewId) {
        if (!reviewRepo.existsById(reviewId)) {
            throw new RuntimeException("Review not found");
        }
        reviewRepo.deleteById(reviewId);
    }
    
    //admin endpoint to get all reviews
    public List<ReviewResponse> getAllReviews() {
        return reviewRepo.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public void rejectReviewAsAdmin(Long reviewId, String reason, String adminUsername) {
        Review review = reviewRepo.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        reviewRepo.delete(review);

        InboxMessage msg = InboxMessage.builder()
                .customerId(review.getCustomerId())
                .customerName(review.getCustomerName())
                .reviewId(review.getReviewId())
                .productId(review.getProductId())
                .title("Your review was removed by admin")
                .message(reason)
                .createdByAdmin(adminUsername)
                .createdAt(LocalDateTime.now())
                .read(false)
                .build();

        inboxMessageRepo.save(msg);
    }

    public List<InboxMessageResponse> getMyInbox(String username) {
        var user = authClient.getUser(username);
        return inboxMessageRepo.findByCustomerIdOrderByCreatedAtDesc(user.userId)
                .stream()
                .map(m -> InboxMessageResponse.builder()
                        .id(m.getId())
                        .reviewId(m.getReviewId())
                        .productId(m.getProductId())
                        .title(m.getTitle())
                        .message(m.getMessage())
                        .createdByAdmin(m.getCreatedByAdmin())
                        .createdAt(m.getCreatedAt())
                        .read(m.isRead())
                        .build())
                .toList();
    }

}