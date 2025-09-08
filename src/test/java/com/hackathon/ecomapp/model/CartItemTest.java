package com.hackathon.ecomapp.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;

class CartItemTest {

    private CartItem cartItem;

    @BeforeEach
    void setUp() {
        cartItem = new CartItem();
    }

    @Test
    void testDefaultConstructor() {
        assertNotNull(cartItem);
        assertNull(cartItem.getProductId());
        assertNull(cartItem.getProductName());
        assertNull(cartItem.getPrice());
        assertNull(cartItem.getQuantity());
        assertNull(cartItem.getCarbonFootprint());
    }

    @Test
    void testParameterizedConstructor() {
        CartItem item = new CartItem("prod123", "Laptop",
                                   new BigDecimal("999.99"), 2, 5.2);

        assertEquals("prod123", item.getProductId());
        assertEquals("Laptop", item.getProductName());
        assertEquals(new BigDecimal("999.99"), item.getPrice());
        assertEquals(2, item.getQuantity());
        assertEquals(5.2, item.getCarbonFootprint());
    }

    @Test
    void testSettersAndGetters() {
        cartItem.setProductId("prod456");
        cartItem.setProductName("Smartphone");
        cartItem.setPrice(new BigDecimal("699.99"));
        cartItem.setQuantity(1);
        cartItem.setCarbonFootprint(3.5);

        assertEquals("prod456", cartItem.getProductId());
        assertEquals("Smartphone", cartItem.getProductName());
        assertEquals(new BigDecimal("699.99"), cartItem.getPrice());
        assertEquals(1, cartItem.getQuantity());
        assertEquals(3.5, cartItem.getCarbonFootprint());
    }

    @Test
    void testGetTotalPrice() {
        cartItem.setPrice(new BigDecimal("100.00"));
        cartItem.setQuantity(3);

        BigDecimal totalPrice = cartItem.getTotalPrice();
        assertEquals(new BigDecimal("300.00"), totalPrice);
    }

    @Test
    void testGetTotalCarbonFootprint() {
        cartItem.setCarbonFootprint(2.5);
        cartItem.setQuantity(4);

        Double totalFootprint = cartItem.getTotalCarbonFootprint();
        assertEquals(10.0, totalFootprint);
    }

    @Test
    void testGetTotalPriceWithZeroQuantity() {
        cartItem.setPrice(new BigDecimal("100.00"));
        cartItem.setQuantity(0);

        BigDecimal totalPrice = cartItem.getTotalPrice();
        assertEquals(new BigDecimal("0.00"), totalPrice);
    }

    @Test
    void testGetTotalCarbonFootprintWithZeroQuantity() {
        cartItem.setCarbonFootprint(2.5);
        cartItem.setQuantity(0);

        Double totalFootprint = cartItem.getTotalCarbonFootprint();
        assertEquals(0.0, totalFootprint);
    }
}
