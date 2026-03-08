package com.example.demo.services;


import java.util.List;
import java.util.Optional;

import com.example.demo.model.Customer;

public interface CustomerService {
	Customer createCustomer(Customer customer);
	Optional<Customer> findByCustomerName(String customerName);
	Customer updateEmail(Integer id, String email);
	Customer updateAddress(Integer id, String address);
	Customer updateMobile(Integer id, String mobileNumber);
	void deleteCustomerById(Integer id);
	List<Customer> getAllCustomers();
}
