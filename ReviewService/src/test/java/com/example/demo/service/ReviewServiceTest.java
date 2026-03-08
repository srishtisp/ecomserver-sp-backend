package com.example.demo.service;

import com.example.demo.client.AuthClient;
import com.example.demo.client.OrderClient;
import com.example.demo.client.ProductClient;
import com.example.demo.client.AuthClient.InternalUserResponse;
import com.example.demo.dto.CreateReviewRequest;
import com.example.demo.dto.ProductInternalDto;
import com.example.demo.entity.Review;
import com.example.demo.repo.InboxMessageRepo;
import com.example.demo.repo.ReviewRepo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepo reviewRepo;

    @Mock
    private AuthClient authClient;

    @Mock
    private ProductClient productClient;

    @Mock
    private OrderClient orderClient;

    @Mock
    private InboxMessageRepo inboxMessageRepo;

    @InjectMocks
    private ReviewService reviewService;

    private CreateReviewRequest request;

    @BeforeEach
    void setup() {
        request = new CreateReviewRequest();
        request.setProductId(1L);
        request.setRating(5);
        request.setComment("Great product");
    }

    @Test
    void shouldCreateReviewSuccessfully() {

        ProductInternalDto product = new ProductInternalDto();
        product.setProductId(1L);
        product.setActive(true);

        InternalUserResponse user = new InternalUserResponse();
        user.userId = 10L;
        user.username = "testuser";

        when(productClient.getProduct(1L)).thenReturn(product);
        when(authClient.getUser("testuser")).thenReturn(user);
        when(orderClient.hasPurchased(10L, 1L)).thenReturn(true);
        when(reviewRepo.existsByProductIdAndCustomerId(1L, 10L)).thenReturn(false);

        reviewService.createReview(request, "testuser");

        verify(reviewRepo).save(any(Review.class));
    }

    @Test
    void shouldThrowIfProductNotActive() {

        ProductInternalDto product = new ProductInternalDto();
        product.setProductId(1L);
        product.setActive(false);

        when(productClient.getProduct(1L)).thenReturn(product);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> reviewService.createReview(request, "user"));

        assertEquals("Product not available for review", ex.getMessage());
    }

    @Test
    void shouldThrowIfUserDidNotPurchase() {

        ProductInternalDto product = new ProductInternalDto();
        product.setProductId(1L);
        product.setActive(true);

        InternalUserResponse user = new InternalUserResponse();
        user.userId = 10L;
        user.username = "testuser";

        when(productClient.getProduct(1L)).thenReturn(product);
        when(authClient.getUser("testuser")).thenReturn(user);
        when(orderClient.hasPurchased(10L, 1L)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> reviewService.createReview(request, "testuser"));

        assertEquals("You must purchase the product before reviewing it", ex.getMessage());
    }

    @Test
    void shouldThrowIfDuplicateReview() {

        ProductInternalDto product = new ProductInternalDto();
        product.setProductId(1L);
        product.setActive(true);

        InternalUserResponse user = new InternalUserResponse();
        user.userId = 10L;
        user.username = "testuser";

        when(productClient.getProduct(1L)).thenReturn(product);
        when(authClient.getUser("testuser")).thenReturn(user);
        when(orderClient.hasPurchased(10L, 1L)).thenReturn(true);
        when(reviewRepo.existsByProductIdAndCustomerId(1L, 10L)).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> reviewService.createReview(request, "testuser"));

        assertEquals("You already reviewed this product", ex.getMessage());
    }

    @Test
    void shouldGetReviewsForProduct() {

        Review review = Review.builder()
                .reviewId(1L)
                .productId(1L)
                .customerName("user")
                .rating(5)
                .comment("Good product")
                .build();

        when(reviewRepo.findByProductId(1L)).thenReturn(List.of(review));

        var response = reviewService.getReviewsForProduct(1L);

        assertEquals(5.0, response.getAverageRating());
        assertEquals(1, response.getReviews().size());
    }

    @Test
    void shouldDeleteOwnReview() {

        InternalUserResponse user = new InternalUserResponse();
        user.userId = 10L;

        when(authClient.getUser("user")).thenReturn(user);
        when(reviewRepo.existsByReviewIdAndCustomerId(1L, 10L)).thenReturn(true);

        reviewService.deleteOwnReview(1L, "user");

        verify(reviewRepo).deleteById(1L);
    }

    @Test
    void shouldThrowIfDeletingOthersReview() {

        InternalUserResponse user = new InternalUserResponse();
        user.userId = 10L;

        when(authClient.getUser("user")).thenReturn(user);
        when(reviewRepo.existsByReviewIdAndCustomerId(1L, 10L)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> reviewService.deleteOwnReview(1L, "user"));

        assertEquals("You can delete only your own review", ex.getMessage());
    }

    @Test
    void shouldRejectReviewAsAdmin() {

        Review review = Review.builder()
                .reviewId(1L)
                .productId(2L)
                .customerId(10L)
                .customerName("user")
                .build();

        when(reviewRepo.findById(1L)).thenReturn(Optional.of(review));

        reviewService.rejectReviewAsAdmin(1L, "Spam review", "admin");

        verify(reviewRepo).delete(review);
        verify(inboxMessageRepo).save(any());
    }
    
    @Test
    void shouldThrowIfProductDoesNotExist() {

        when(productClient.getProduct(1L)).thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> reviewService.createReview(request, "user"));

        assertEquals("Product not available for review", ex.getMessage());
    }
    
    @Test
    void shouldReturnZeroRatingWhenNoReviews() {

        when(reviewRepo.findByProductId(1L)).thenReturn(List.of());

        var response = reviewService.getReviewsForProduct(1L);

        assertEquals(0.0, response.getAverageRating());
        assertTrue(response.getReviews().isEmpty());
    }
    
    @Test
    void shouldThrowIfAdminDeletesNonexistentReview() {

        when(reviewRepo.existsById(1L)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> reviewService.deleteReviewAsAdmin(1L));

        assertEquals("Review not found", ex.getMessage());
    }
    
    @Test
    void shouldReturnEmptyInbox() {

        InternalUserResponse user = new InternalUserResponse();
        user.userId = 10L;

        when(authClient.getUser("user")).thenReturn(user);
        when(inboxMessageRepo.findByCustomerIdOrderByCreatedAtDesc(10L))
                .thenReturn(List.of());

        var inbox = reviewService.getMyInbox("user");

        assertTrue(inbox.isEmpty());
    }
    
    @Test
    void shouldReturnAllReviewsForAdmin() {

        Review review = Review.builder()
                .reviewId(1L)
                .productId(1L)
                .customerName("user")
                .rating(4)
                .comment("Good")
                .build();

        when(reviewRepo.findAll()).thenReturn(List.of(review));

        var result = reviewService.getAllReviews();

        assertEquals(1, result.size());
    }
    
    @Test
    void shouldReturnInboxMessages() {

        InternalUserResponse user = new InternalUserResponse();
        user.userId = 10L;

        when(authClient.getUser("user")).thenReturn(user);

        var message = com.example.demo.entity.InboxMessage.builder()
                .id(1L)
                .reviewId(1L)
                .productId(1L)
                .title("Removed")
                .message("Spam")
                .createdByAdmin("admin")
                .read(false)
                .build();

        when(inboxMessageRepo.findByCustomerIdOrderByCreatedAtDesc(10L))
                .thenReturn(List.of(message));

        var result = reviewService.getMyInbox("user");

        assertEquals(1, result.size());
    }

}