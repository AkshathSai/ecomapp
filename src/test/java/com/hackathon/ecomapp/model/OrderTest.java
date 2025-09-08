package com.hackathon.ecomapp.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

class OrderTest {

    private Order order;
    private List<CartItem> testItems;

    @BeforeEach
    void setUp() {
        CartItem item1 = new CartItem("prod1", "Laptop", new BigDecimal("1000.00"), 1, 5.0);
        CartItem item2 = new CartItem("prod2", "Mouse", new BigDecimal("50.00"), 2, 1.0);
        testItems = Arrays.asList(item1, item2);
        order = new Order();
    }

    @Test
    void testDefaultConstructor() {
        assertNotNull(order);
        assertNull(order.getId());
        assertNull(order.getUserId());
        assertEquals(BigDecimal.ZERO, order.getDiscount());
        assertEquals(Order.OrderStatus.PENDING, order.getStatus());
        assertNotNull(order.getOrderDate());
    }

    @Test
    void testParameterizedConstructor() {
        Order order = new Order("user123", testItems);

        assertEquals("user123", order.getUserId());
        assertEquals(testItems, order.getItems());
        assertEquals(Order.OrderStatus.PENDING, order.getStatus());
        assertNotNull(order.getOrderDate());
        assertEquals(new BigDecimal("1100.00"), order.getSubtotal()); // 1000 + (50*2)
        assertEquals(7.0, order.getTotalCarbonFootprint()); // 5.0 + (1.0*2)
    }

    @Test
    void testSettersAndGetters() {
        order.setId("order123");
        order.setUserId("user456");
        order.setPromotionCode("SAVE10");
        order.setStatus(Order.OrderStatus.CONFIRMED);

        assertEquals("order123", order.getId());
        assertEquals("user456", order.getUserId());
        assertEquals("SAVE10", order.getPromotionCode());
        assertEquals(Order.OrderStatus.CONFIRMED, order.getStatus());
    }

    @Test
    void testSetItems() {
        order.setItems(testItems);

        assertEquals(testItems, order.getItems());
        assertEquals(new BigDecimal("1100.00"), order.getSubtotal());
        assertEquals(7.0, order.getTotalCarbonFootprint());
    }

    @Test
    void testSetDiscount() {
        order.setSubtotal(new BigDecimal("1000.00"));
        order.setDiscount(new BigDecimal("100.00"));

        assertEquals(new BigDecimal("100.00"), order.getDiscount());
        assertEquals(new BigDecimal("900.00"), order.getTotalAmount());
    }

    @Test
    void testSetTotalAmount() {
        order.setTotalAmount(new BigDecimal("500.00"));
        assertEquals(new BigDecimal("500.00"), order.getTotalAmount());
    }

    @Test
    void testSetTotalCarbonFootprint() {
        order.setTotalCarbonFootprint(10.5);
        assertEquals(10.5, order.getTotalCarbonFootprint());
    }

    @Test
    void testOrderStatus() {
        order.setStatus(Order.OrderStatus.SHIPPED);
        assertEquals(Order.OrderStatus.SHIPPED, order.getStatus());

        order.setStatus(Order.OrderStatus.DELIVERED);
        assertEquals(Order.OrderStatus.DELIVERED, order.getStatus());

        order.setStatus(Order.OrderStatus.CANCELLED);
        assertEquals(Order.OrderStatus.CANCELLED, order.getStatus());
    }

    @Test
    void testOrderDate() {
        LocalDateTime customDate = LocalDateTime.of(2023, 1, 1, 10, 0);
        order.setOrderDate(customDate);
        assertEquals(customDate, order.getOrderDate());
    }
}
