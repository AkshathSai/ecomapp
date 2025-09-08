package com.hackathon.ecomapp.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EmptyCartExceptionTest {

    @Test
    void testExceptionMessage() {
        EmptyCartException exception = new EmptyCartException("User cart is empty");

        assertEquals("User cart is empty", exception.getMessage());
    }

    @Test
    void testExceptionWithCustomMessage() {
        EmptyCartException exception = new EmptyCartException("Cannot proceed with checkout - cart is empty");

        assertEquals("Cannot proceed with checkout - cart is empty", exception.getMessage());
    }

    @Test
    void testExceptionWithNullMessage() {
        EmptyCartException exception = new EmptyCartException(null);

        assertNull(exception.getMessage());
    }
}
