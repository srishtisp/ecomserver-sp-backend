package com.example.demo;

import com.example.demo.client.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class AdminServiceIntegrationTest {

    @MockBean private ProductClient productClient;
    @MockBean private VendorClient vendorClient;
    @MockBean private CustomerClient customerClient;
    @MockBean private OrderClient orderClient;
    @MockBean private PaymentClient paymentClient;
    @MockBean private ReviewClient reviewClient;

    @Test
    void contextLoads() {
    }
}
