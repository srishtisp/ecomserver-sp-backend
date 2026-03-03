package com.example.demo.service;

import com.example.demo.dto.*;

import java.util.List;

public interface ProductService {

    // browsing
    List<ProductResponse> viewAll();
    List<ProductResponse> viewByCategory(String category);
    List<ProductResponse> viewByProductName(String productName);
    List<ProductResponse> viewByVendorName(String vendorName);

    // direct
    ProductResponse getById(Long id);

    // internal for order-service
    ProductInternalDTO getInternal(Long id);

    // vendor/admin operations
    ProductResponse create(ProductCreateRequest req);
    ProductResponse update(Long id, ProductUpdateRequest req);
    void delete(Long id);
    void toggleStatus(Long id, boolean active);
}