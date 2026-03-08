package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.Customer;
import com.example.demo.services.CustomerService;

//@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;
    
    @PostMapping
    public Customer createCustomer(@RequestBody Customer customer) {
        return customerService.createCustomer(customer);
    }

    @GetMapping("/name/{name}")
    public Optional<Customer> getByName(@PathVariable String name) {
        return customerService.findByCustomerName(name);
    }

    @PatchMapping("/{id}/email")
    public Customer updateEmail(@PathVariable Integer id,
                                @RequestBody String email) {
        return customerService.updateEmail(id, email);
    }

    @PatchMapping("/{id}/address")
    public Customer updateAddress(@PathVariable Integer id,
                                  @RequestBody String address) {
        return customerService.updateAddress(id, address);
    }

    @PatchMapping("/{id}/mobile")
    public Customer updateMobile(@PathVariable Integer id,
                                 @RequestBody String mobile) {
        return customerService.updateMobile(id, mobile);
    }
    @DeleteMapping("/{id}")
    public void deleteCustomer(@PathVariable Integer id) {
        customerService.deleteCustomerById(id);
    }
    
    @GetMapping("/admin")
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }
}