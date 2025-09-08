package com.hackathon.ecomapp.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ResourceNotFoundExceptionTest {

    @Test
    void testExceptionMessage() {
        ResourceNotFoundException exception = new ResourceNotFoundException("Product", "id", "123");

        assertEquals("Product not found with id: '123'", exception.getMessage());
    }

    @Test
    void testExceptionWithDifferentResource() {
        ResourceNotFoundException exception = new ResourceNotFoundException("Order", "orderId", "order456");

        assertEquals("Order not found with orderId: 'order456'", exception.getMessage());
    }

    @Test
    void testExceptionWithNullValue() {
        ResourceNotFoundException exception = new ResourceNotFoundException("User", "email", null);

        assertEquals("User not found with email: 'null'", exception.getMessage());
    }

    @Test
    void testExceptionWithNumericValue() {
        ResourceNotFoundException exception = new ResourceNotFoundException("Cart", "id", 789);

        assertEquals("Cart not found with id: '789'", exception.getMessage());
    }
}
