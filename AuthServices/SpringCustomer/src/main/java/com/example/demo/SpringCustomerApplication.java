package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SpringCustomerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringCustomerApplication.class, args);
		System.out.println("Customer is Up and running..!");
	}

}
