package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // ✅ PUBLIC browsing APIs (your customer stories)
    @GetMapping("/view")
    public List<ProductResponse> view() {
        return productService.viewAll();
    }

    @GetMapping("/viewByCategory")
    public List<ProductResponse> viewByCategory(@RequestParam String category) {
        return productService.viewByCategory(category);
    }

    @GetMapping("/viewByProductName")
    public List<ProductResponse> viewByProductName(@RequestParam String productName) {
        return productService.viewByProductName(productName);
    }

    @GetMapping("/viewByVendorName")
    public List<ProductResponse> viewByVendorName(@RequestParam String vendorName) {
        return productService.viewByVendorName(vendorName);
    }

    @GetMapping("/{id}")
    public ProductResponse getById(@PathVariable Long id) {
        return productService.getById(id);
    }

    // ✅ INTERNAL endpoint for Order service (price calculation)
    @GetMapping("/internal/{id}")
    public ProductInternalDTO internal(@PathVariable Long id) {
        return productService.getInternal(id);
    }

    // ✅ SECURED endpoints (Vendor/Admin)
    @PostMapping
    public ProductResponse create(@Valid @RequestBody ProductCreateRequest req) {
        return productService.create(req);
    }

    @PutMapping("/{id}")
    public ProductResponse update(@PathVariable Long id, @Valid @RequestBody ProductUpdateRequest req) {
        return productService.update(id, req);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        productService.delete(id);
        return "Deleted productId=" + id;
    }

    @PatchMapping("/{id}/status")
    public String toggle(@PathVariable Long id, @RequestParam boolean active) {
        productService.toggleStatus(id, active);
        return "Product status updated: productId=" + id + ", active=" + active;
    }
    
}