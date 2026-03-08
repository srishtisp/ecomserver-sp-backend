package com.example.demo.dto;

import lombok.Data;

@Data
public class CustomerDto {

    private Integer id;
    private String customerName;
    private String mobileNumber;
    private String email;
    private String address;

}
