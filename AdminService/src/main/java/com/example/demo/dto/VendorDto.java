package com.example.demo.dto;

import lombok.Data;

@Data
public class VendorDto {

    private Integer id;
    private String vendorName;
    private String mobileNumber;
    private String email;
    private String address;
    private String category;

}