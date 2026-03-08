package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AdminServiceApplicationTest {

    @Test
    void contextLoads() {
        // Just checks if application context starts
    }

    @Test
    void mainMethodRuns() {
        AdminServiceApplication.main(new String[] {});
    }
}