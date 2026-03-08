package com.example.demo.controller;

import com.example.demo.dto.CreateReviewRequest;
import com.example.demo.dto.RejectReviewRequest;
import com.example.demo.service.ReviewService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.security.core.Authentication;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ReviewControllerTest {

    @Mock
    private ReviewService reviewService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private ReviewController reviewController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateReview() {

        CreateReviewRequest request = new CreateReviewRequest();
        request.setProductId(1L);
        request.setRating(5);
        request.setComment("Great product");

        when(authentication.getName()).thenReturn("user");

        var response = reviewController.createReview(request, authentication);

        verify(reviewService).createReview(request, "user");
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void shouldGetProductReviews() {

        reviewController.getProductReviews(1L);

        verify(reviewService).getReviewsForProduct(1L);
    }

    @Test
    void shouldDeleteOwnReview() {

        when(authentication.getName()).thenReturn("user");

        var response = reviewController.deleteOwnReview(1L, authentication);

        verify(reviewService).deleteOwnReview(1L, "user");
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void shouldRejectReviewAsAdmin() {

        RejectReviewRequest request = new RejectReviewRequest();
        request.setReason("Spam");

        when(authentication.getName()).thenReturn("admin");

        var response = reviewController.rejectReviewAsAdmin(1L, request, authentication);

        verify(reviewService).rejectReviewAsAdmin(1L, "Spam", "admin");
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void shouldListReviewsForAdmin() {

        reviewController.listReviewsForAdmin();

        verify(reviewService).getAllReviews();
    }

}