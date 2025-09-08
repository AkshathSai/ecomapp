package com.hackathon.ecomapp.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InsufficientStockExceptionTest {

    @Test
    void testExceptionMessage() {
        InsufficientStockException exception = new InsufficientStockException("Laptop", 5, 2);

        assertEquals("Insufficient stock for product 'Laptop'. Requested: 5, Available: 2",
                    exception.getMessage());
    }

    @Test
    void testExceptionWithZeroStock() {
        InsufficientStockException exception = new InsufficientStockException("Smartphone", 1, 0);

        assertEquals("Insufficient stock for product 'Smartphone'. Requested: 1, Available: 0",
                    exception.getMessage());
    }

    @Test
    void testExceptionWithLargeQuantities() {
        InsufficientStockException exception = new InsufficientStockException("Gaming Console", 100, 25);

        assertEquals("Insufficient stock for product 'Gaming Console'. Requested: 100, Available: 25",
                    exception.getMessage());
    }
}
