package com.example.demo.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
import com.example.demo.dto.CustomerDto;

@FeignClient(name = "SpringCustomer")
public interface CustomerClient {

    @GetMapping("/customers/admin")
    List<CustomerDto> listCustomers();
}