package com.example.demo.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.dto.VendorDto;

@FeignClient(name = "SpringVendor")
public interface VendorClient {

    @GetMapping("/vendors/admin")
    List<VendorDto> getAllVendors();
}
