package com.example.demo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Vendor;
import com.example.demo.repo.VendorRepo;

@Service
public class VendorServiceImpl implements VendorService{

	@Autowired
    private VendorRepo vendorRepo;

    @Override
    public Vendor createVendor(Vendor vendor) {
        return vendorRepo.save(vendor);
    }

    @Override
    public Optional<Vendor> findByVendorName(String vendorName) {
        return vendorRepo.findByVendorName(vendorName);
    }

    @Override
    public Vendor updateEmail(Integer id, String email) {
        Vendor vendor = vendorRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));

        vendor.setEmail(email);
        return vendorRepo.save(vendor);
    }

    @Override
    public Vendor updateAddress(Integer id, String address) {
        Vendor vendor = vendorRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));

        vendor.setAddress(address);
        return vendorRepo.save(vendor);
    }

    @Override
    public Vendor updateMobile(Integer id, String mobileNumber) {
        Vendor vendor = vendorRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));

        vendor.setMobileNumber(mobileNumber);
        return vendorRepo.save(vendor);
    }

    @Override
    public void deleteVendorById(Integer id) {
        if (!vendorRepo.existsById(id)) {
            throw new RuntimeException("Vendor not found");
        }
        vendorRepo.deleteById(id);
    }

    @Override
    public Vendor updateCategory(Integer id, String category) {

        Vendor vendor = vendorRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));

        vendor.setCategory(category);
        return vendorRepo.save(vendor);
    }
    public Vendor getVendorById(Integer id){
        return vendorRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Vendor not found"));
    }
    
    @Override
    public List<Vendor> getAllVendors() {
        return vendorRepo.findAll();
    }
}
