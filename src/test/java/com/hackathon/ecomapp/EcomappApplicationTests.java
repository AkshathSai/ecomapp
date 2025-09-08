package com.hackathon.ecomapp;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class EcomappApplicationTests {

    @Test
    void contextLoads() {
        // This test verifies that the Spring context loads successfully
        // In CI environment, it will use MongoDB and Kafka containers
        // For local testing without Docker, this will still pass with proper configuration
    }

}
