package com.hackathon.ecomapp.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

class CartTest {

    private Cart cart;

    @BeforeEach
    void setUp() {
        cart = new Cart();
    }

    @Test
    void testDefaultConstructor() {
        assertNotNull(cart);
        assertNull(cart.getId());
        assertNull(cart.getUserId());
        assertNotNull(cart.getItems());
        assertTrue(cart.getItems().isEmpty());
        assertNotNull(cart.getCreatedAt());
        assertNotNull(cart.getUpdatedAt());
    }

    @Test
    void testParameterizedConstructor() {
        Cart cart = new Cart("user123");

        assertEquals("user123", cart.getUserId());
        assertNotNull(cart.getItems());
        assertTrue(cart.getItems().isEmpty());
        assertNotNull(cart.getCreatedAt());
        assertNotNull(cart.getUpdatedAt());
    }

    @Test
    void testAddItem() {
        CartItem item = new CartItem("prod123", "Laptop",
                                   new BigDecimal("999.99"), 1, 5.2);

        LocalDateTime beforeAdd = cart.getUpdatedAt();
        cart.addItem(item);

        assertEquals(1, cart.getItems().size());
        assertTrue(cart.getUpdatedAt().isAfter(beforeAdd) || cart.getUpdatedAt().isEqual(beforeAdd));
    }

    @Test
    void testGetTotalAmountEmptyCart() {
        BigDecimal total = cart.getTotalAmount();
        assertEquals(BigDecimal.ZERO, total);
    }

    @Test
    void testGetTotalAmountWithItems() {
        CartItem item1 = new CartItem("prod1", "Laptop",
                                     new BigDecimal("1000.00"), 1, 5.0);
        CartItem item2 = new CartItem("prod2", "Mouse",
                                     new BigDecimal("50.00"), 2, 1.0);

        cart.addItem(item1);
        cart.addItem(item2);

        BigDecimal expectedTotal = new BigDecimal("1100.00");
        assertEquals(expectedTotal, cart.getTotalAmount());
    }

    @Test
    void testGetTotalCarbonFootprintEmptyCart() {
        Double footprint = cart.getTotalCarbonFootprint();
        assertEquals(0.0, footprint);
    }

    @Test
    void testGetTotalCarbonFootprintWithItems() {
        CartItem item1 = new CartItem("prod1", "Laptop",
                                     new BigDecimal("1000.00"), 1, 5.0);
        CartItem item2 = new CartItem("prod2", "Mouse",
                                     new BigDecimal("50.00"), 2, 1.0);

        cart.addItem(item1);
        cart.addItem(item2);

        Double expectedFootprint = 7.0; // 5.0 + (1.0 * 2)
        assertEquals(expectedFootprint, cart.getTotalCarbonFootprint());
    }

    @Test
    void testSettersAndGetters() {
        cart.setId("cart123");
        cart.setUserId("user456");

        assertEquals("cart123", cart.getId());
        assertEquals("user456", cart.getUserId());
    }
}
