package com.example.demo.impl;

import com.example.demo.dto.*;
import com.example.demo.entity.Product;
import com.example.demo.repo.ProductRepo;
import com.example.demo.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepo repo;

    // ---- mapping helpers ----
    private ProductResponse toResponse(Product p) {
        return ProductResponse.builder()
                .productId(p.getProductId())
                .productName(p.getProductName())
                .description(p.getDescription())
                .price(p.getPrice())
                .vendorId(p.getVendorId())
                .vendorName(p.getVendorName())
                .category(p.getCategory())
                .active(p.isActive())
                .build();
    }

    private ProductInternalDTO toInternal(Product p) {
        return ProductInternalDTO.builder()
                .productId(p.getProductId())
                .productName(p.getProductName())
                .price(p.getPrice())
                .active(p.isActive())
                .build();
    }

    // ---- browsing ----
    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> viewAll() {
        return repo.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> viewByCategory(String category) {
        return repo.findByCategoryIgnoreCase(category).stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> viewByProductName(String productName) {
        return repo.findByProductNameContainingIgnoreCase(productName).stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> viewByVendorName(String vendorName) {
        return repo.findByVendorNameContainingIgnoreCase(vendorName).stream().map(this::toResponse).toList();
    }

    // ---- direct ----
    @Override
    @Transactional(readOnly = true)
    public ProductResponse getById(Long id) {
        Product p = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found: " + id));
        return toResponse(p);
    }

    // ---- internal for order-service ----
    @Override
    @Transactional(readOnly = true)
    public ProductInternalDTO getInternal(Long id) {
        Product p = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found: " + id));
        return toInternal(p);
    }

    // ---- vendor/admin operations ----
    @Override
    public ProductResponse create(ProductCreateRequest req) {

        Product p = Product.builder()
                .productName(req.getProductName())
                .description(req.getDescription())
                .price(req.getPrice())
                .vendorId(req.getVendorId())
                .vendorName(req.getVendorName())
                .category(req.getCategory())
                .active(true)
                .build();

        return toResponse(repo.save(p));
    }

    @Override
    public ProductResponse update(Long id, ProductUpdateRequest req) {

        Product p = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found: " + id));

        p.setProductName(req.getProductName());
        p.setDescription(req.getDescription());
        p.setPrice(req.getPrice());
        p.setVendorId(req.getVendorId());
        p.setVendorName(req.getVendorName());
        p.setCategory(req.getCategory());

        return toResponse(repo.save(p));
    }

    @Override
    public void delete(Long id) {
        if (!repo.existsById(id)) throw new RuntimeException("Product not found: " + id);
        repo.deleteById(id);
    }

    @Override
    public void toggleStatus(Long id, boolean active) {
        Product p = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found: " + id));
        p.setActive(active);
        repo.save(p);
    }
}