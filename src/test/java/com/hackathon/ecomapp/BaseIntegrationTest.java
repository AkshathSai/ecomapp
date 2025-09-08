package com.hackathon.ecomapp;

import com.hackathon.ecomapp.config.TestContainersConfig;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

/**
 * Base class for integration tests that require MongoDB and Kafka.
 * Extend this class in your integration tests to automatically get containerized dependencies.
 */
@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes = TestContainersConfig.class)
public abstract class BaseIntegrationTest {
    // This class provides the foundation for integration tests with real MongoDB and Kafka instances
}
