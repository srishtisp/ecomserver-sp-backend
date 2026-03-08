package com.example.demo.controller;

import com.example.demo.client.*;
import com.example.demo.dto.*;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

import org.mockito.MockitoAnnotations;

import java.util.List;

class AdminControllerTest {

    @Mock
    private ProductClient productClient;

    @Mock
    private VendorClient vendorClient;

    @Mock
    private CustomerClient customerClient;

    @Mock
    private OrderClient orderClient;

    @Mock
    private PaymentClient paymentClient;

    @Mock
    private ReviewClient reviewClient;

    @InjectMocks
    private AdminController adminController;

    public AdminControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testApproveProduct() {
        Long id = 1L;

        var response = adminController.approveProduct(id);

        Mockito.verify(productClient).approveProduct(id);
        assertEquals("Product approved successfully", response.getBody());
    }

    @Test
    void testDisapproveProduct() {
        Long id = 1L;

        var response = adminController.disapproveProduct(id);

        Mockito.verify(productClient).disapproveProduct(id);
        assertEquals("Product disapproved successfully", response.getBody());
    }

    @Test
    void testRejectReview() {
        Long reviewId = 1L;
        RejectReviewRequest req = new RejectReviewRequest();

        Mockito.when(reviewClient.rejectReview(reviewId, req))
                .thenReturn("Review rejected");

        var response = adminController.rejectReview(reviewId, req);

        assertEquals("Review rejected", response.getBody());
    }

    @Test
    void testListVendors() {

        Mockito.when(vendorClient.getAllVendors())
                .thenReturn(List.of(new VendorDto()));

        List<VendorDto> vendors = adminController.listVendors();

        assertEquals(1, vendors.size());
    }

    @Test
    void testListProducts() {

        Mockito.when(productClient.getAllProductsForAdmin())
                .thenReturn(List.of(new ProductDto()));

        List<ProductDto> products = adminController.listProducts();

        assertEquals(1, products.size());
    }

    @Test
    void testListCustomers() {

        Mockito.when(customerClient.listCustomers())
                .thenReturn(List.of(new CustomerDto()));

        List<CustomerDto> customers = adminController.getCustomers();

        assertEquals(1, customers.size());
    }

    @Test
    void testListOrders() {

        Mockito.when(orderClient.getAllOrders())
                .thenReturn(List.of(new OrderDto()));

        List<OrderDto> orders = adminController.listOrders();

        assertEquals(1, orders.size());
    }

    @Test
    void testListPayments() {

        Mockito.when(paymentClient.getAllPayments())
                .thenReturn(List.of(new PaymentDto()));

        List<PaymentDto> payments = adminController.listPayments();

        assertEquals(1, payments.size());
    }

    @Test
    void testListReviews() {

        Mockito.when(reviewClient.getAllReviewsForAdmin())
                .thenReturn(List.of(new ReviewDto()));

        List<ReviewDto> reviews = adminController.listReviews();

        assertEquals(1, reviews.size());
    }
}