package com.hackathon.ecomapp.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;

class ProductTest {

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
    }

    @Test
    void testDefaultConstructor() {
        assertNotNull(product);
        assertNull(product.getId());
        assertNull(product.getName());
        assertNull(product.getDescription());
        assertNull(product.getPrice());
        assertNull(product.getStock());
        assertNull(product.getCarbonFootprint());
    }

    @Test
    void testParameterizedConstructor() {
        Product product = new Product("Laptop", "Gaming laptop",
                                    new BigDecimal("999.99"), 10, 5.2);

        assertEquals("Laptop", product.getName());
        assertEquals("Gaming laptop", product.getDescription());
        assertEquals(new BigDecimal("999.99"), product.getPrice());
        assertEquals(10, product.getStock());
        assertEquals(5.2, product.getCarbonFootprint());
    }

    @Test
    void testSettersAndGetters() {
        product.setId("prod123");
        product.setName("Smartphone");
        product.setDescription("Latest smartphone");
        product.setPrice(new BigDecimal("699.99"));
        product.setStock(25);
        product.setCarbonFootprint(3.5);

        assertEquals("prod123", product.getId());
        assertEquals("Smartphone", product.getName());
        assertEquals("Latest smartphone", product.getDescription());
        assertEquals(new BigDecimal("699.99"), product.getPrice());
        assertEquals(25, product.getStock());
        assertEquals(3.5, product.getCarbonFootprint());
    }

    @Test
    void testPriceWithZeroValue() {
        product.setPrice(BigDecimal.ZERO);
        assertEquals(BigDecimal.ZERO, product.getPrice());
    }

    @Test
    void testStockWithZeroValue() {
        product.setStock(0);
        assertEquals(0, product.getStock());
    }

    @Test
    void testCarbonFootprintWithZeroValue() {
        product.setCarbonFootprint(0.0);
        assertEquals(0.0, product.getCarbonFootprint());
    }
}
