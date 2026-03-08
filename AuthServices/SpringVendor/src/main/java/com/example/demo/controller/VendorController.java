package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Vendor;
import com.example.demo.services.VendorService;

//	@CrossOrigin("http://localhost:4200")
	@RestController
	@RequestMapping("/vendors")
	public class VendorController {

	    @Autowired
	    private VendorService vendorService;
	    
	    @PostMapping
	    public Vendor createVendor(@RequestBody Vendor vendor) {
	        return vendorService.createVendor(vendor);
	    }

	    @GetMapping("/name/{name}")
	    public Optional<Vendor> getByName(@PathVariable String name) {
	        return vendorService.findByVendorName(name);
	    }

	    @PatchMapping("/{id}/email")
	    public Vendor updateEmail(@PathVariable Integer id,
	                                @RequestBody String email) {
	        return vendorService.updateEmail(id, email);
	    }

	    @PatchMapping("/{id}/address")
	    public Vendor updateAddress(@PathVariable Integer id,
	                                  @RequestBody String address) {
	        return vendorService.updateAddress(id, address);
	    }

	    @PatchMapping("/{id}/mobile")
	    public Vendor updateMobile(@PathVariable Integer id,
	                                 @RequestBody String mobile) {
	        return vendorService.updateMobile(id, mobile);
	    }
	    @DeleteMapping("/{id}")
	    public void deleteVendor(@PathVariable Integer id) {
	    	vendorService.deleteVendorById(id);
	    }
	    @GetMapping("/{id}")
	    public Vendor getById(@PathVariable Integer id) {
	        return vendorService.getVendorById(id);
	    }
	    @GetMapping("/admin")
	    public List<Vendor> getAllVendors() {
	        return vendorService.getAllVendors();
	    }
	}
