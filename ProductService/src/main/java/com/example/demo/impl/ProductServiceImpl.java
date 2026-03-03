package com.example.demo.impl;

import com.example.demo.dto.*;

import com.example.demo.entity.Product;
import com.example.demo.repo.ProductRepo;
import com.example.demo.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {
	@Value("${app.upload.dir:uploads}")
	private String uploadDir;

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
    @Override
    public void saveImageToFolder(Long productId, MultipartFile file) throws Exception {

        if (file == null || file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new RuntimeException("Only image files allowed");
        }

        Product p = repo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found: " + productId));

        // ✅ create uploads folder
        Path dir = Paths.get(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(dir);

        // ✅ file extension
        String ext = switch (contentType) {
            case "image/png" -> "png";
            case "image/jpeg" -> "jpg";
            case "image/webp" -> "webp";
            default -> "img";
        };

        String filename = "product-" + productId + "." + ext;
        Path target = dir.resolve(filename);

        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

        // ✅ save path + type in DB
        p.setImagePath(target.toString());
        p.setImageType(contentType);
        repo.save(p);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<byte[]> getImageBytes(Long productId) throws Exception {

        Product p = repo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found: " + productId));

        if (p.getImagePath() == null || p.getImagePath().isBlank()) {
            return ResponseEntity.notFound().build();
        }

        Path path = Paths.get(p.getImagePath());

        if (!Files.exists(path)) {
            return ResponseEntity.notFound().build();
        }

        byte[] bytes = Files.readAllBytes(path);

        MediaType type = (p.getImageType() != null && !p.getImageType().isBlank())
                ? MediaType.parseMediaType(p.getImageType())
                : MediaType.APPLICATION_OCTET_STREAM;

        return ResponseEntity.ok()
                .contentType(type)
                .body(bytes);
    }
}