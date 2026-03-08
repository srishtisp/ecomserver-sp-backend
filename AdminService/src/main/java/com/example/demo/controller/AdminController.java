package com.example.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.client.CustomerClient;
import com.example.demo.client.OrderClient;
import com.example.demo.client.PaymentClient;
import com.example.demo.client.ProductClient;
import com.example.demo.client.ReviewClient;
import com.example.demo.client.VendorClient;
import com.example.demo.dto.CustomerDto;
import com.example.demo.dto.OrderDto;
import com.example.demo.dto.PaymentDto;
import com.example.demo.dto.ProductDto;
import com.example.demo.dto.RejectReviewRequest;
import com.example.demo.dto.ReviewDto;
import com.example.demo.dto.VendorDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ProductClient productClient;
    private final VendorClient vendorClient;
    private final CustomerClient customerClient;
    private final OrderClient orderClient;
    private final PaymentClient paymentClient;
    private final ReviewClient reviewClient;

    // 9️⃣ Approve product
    @PutMapping("/approve/{id}")
    public ResponseEntity<String> approveProduct(@PathVariable Long id) {
        productClient.approveProduct(id);
        return ResponseEntity.ok("Product approved successfully");
    }

    @PutMapping("/disapprove/{id}")
    public ResponseEntity<String> disapproveProduct(@PathVariable Long id) {
        productClient.disapproveProduct(id);
        return ResponseEntity.ok("Product disapproved successfully");
    }
    
    @DeleteMapping("/rejectReview/{reviewId}")
    public ResponseEntity<String> rejectReview(
            @PathVariable Long reviewId,
            @RequestBody RejectReviewRequest request) {

        String msg = reviewClient.rejectReview(reviewId, request);
        return ResponseEntity.ok(msg);
    }

    // 11️⃣ 1. List vendors
    @GetMapping("/listVendors")
    public List<VendorDto> listVendors() {
        return vendorClient.getAllVendors();
    }

    // 11️⃣ 2. List products
    @GetMapping("/listProducts")
    public List<ProductDto> listProducts() {
        return productClient.getAllProductsForAdmin();
    }
    
    @GetMapping("/listCustomers")
    public List<CustomerDto> getCustomers() {
        return customerClient.listCustomers();
    }
    
    @GetMapping("/listOrders")
    public List<OrderDto> listOrders() {
        return orderClient.getAllOrders();
    }
    
    @GetMapping("/listPayments")
    public List<PaymentDto> listPayments() {
        return paymentClient.getAllPayments();
    }
    
    @GetMapping("/listReviews")
    public List<ReviewDto> listReviews() {
        return reviewClient.getAllReviewsForAdmin();
    }
}