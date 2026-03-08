package com.example.demo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Customer;
import com.example.demo.repo.CustomerRepo;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepo customerRepo;
    
    @Override
    public Customer createCustomer(Customer customer) {
        return customerRepo.save(customer);
    }

    @Override
    public Optional<Customer> findByCustomerName(String customerName) {
        return customerRepo.findByCustomerName(customerName);
    }

    @Override
    public Customer updateEmail(Integer id, String email) {

        Customer customer = customerRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        customer.setEmail(email);
        return customerRepo.save(customer);
    }

    @Override
    public Customer updateAddress(Integer id, String address) {

        Customer customer = customerRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        customer.setAddress(address);
        return customerRepo.save(customer);
    }

    @Override
    public Customer updateMobile(Integer id, String mobileNumber) {

        Customer customer = customerRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        customer.setMobileNumber(mobileNumber);
        return customerRepo.save(customer);
    }

    @Override
    public void deleteCustomerById(Integer id) {

        if (!customerRepo.existsById(id)) {
            throw new RuntimeException("Customer not found");
        }

        customerRepo.deleteById(id);
    }
    
    @Override
    public List<Customer> getAllCustomers() {
        return customerRepo.findAll();
    }

}