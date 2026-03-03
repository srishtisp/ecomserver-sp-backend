package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SpringVendorApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringVendorApplication.class, args);
		System.out.println("Vendor up and running..!");
	}

}
