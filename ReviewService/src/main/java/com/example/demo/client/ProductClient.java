package com.example.demo.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.dto.ProductInternalDto;

@FeignClient(name = "ProductService")
public interface ProductClient {

    @GetMapping("/products/internal/{productId}")
    ProductInternalDto getProduct(@PathVariable Long productId);
}
