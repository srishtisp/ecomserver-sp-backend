package com.example.demo.services;

import java.util.List;
import java.util.Optional;

import com.example.demo.model.Vendor;


public interface VendorService {
	Vendor createVendor(Vendor vendor);
	Optional<Vendor> findByVendorName(String vendorName);
	Vendor updateEmail(Integer id, String email);
	Vendor updateAddress(Integer id, String address);
	Vendor updateMobile(Integer id, String mobileNumber);
	Vendor updateCategory(Integer id,String category);
	void deleteVendorById(Integer id);
	Vendor getVendorById(Integer id);
	List<Vendor> getAllVendors();
}
