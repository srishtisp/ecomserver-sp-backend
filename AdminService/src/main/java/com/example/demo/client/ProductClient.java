package com.example.demo.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import com.example.demo.dto.ProductDto;

@FeignClient(name = "productservice")
public interface ProductClient {

	@PutMapping("/products/approve/{id}")
	void approveProduct(@PathVariable Long id);

	@PutMapping("/products/disapprove/{id}")
	void disapproveProduct(@PathVariable Long id);

	@GetMapping("/products/admin")
	List<ProductDto> getAllProductsForAdmin();
}
