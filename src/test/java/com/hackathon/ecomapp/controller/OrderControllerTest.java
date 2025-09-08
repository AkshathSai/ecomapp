package com.hackathon.ecomapp.controller;

import com.hackathon.ecomapp.model.Order;
import com.hackathon.ecomapp.service.OrderService;
import com.hackathon.ecomapp.exception.EmptyCartException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    private Order testOrder;

    @BeforeEach
    void setUp() {
        testOrder = new Order();
        testOrder.setId("order123");
        testOrder.setUserId("user123");
    }

    @Test
    void testPlaceOrder_Success() throws Exception {
        when(orderService.createOrder("user123", null)).thenReturn(testOrder);

        mockMvc.perform(post("/api/orders/place")
                .param("userId", "user123"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("order123"))
                .andExpect(jsonPath("$.userId").value("user123"));

        verify(orderService).createOrder("user123", null);
    }

    @Test
    void testPlaceOrder_WithPromotionCode() throws Exception {
        when(orderService.createOrder("user123", "SAVE10")).thenReturn(testOrder);

        mockMvc.perform(post("/api/orders/place")
                .param("userId", "user123")
                .param("promotionCode", "SAVE10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("order123"));

        verify(orderService).createOrder("user123", "SAVE10");
    }

    @Test
    void testPlaceOrder_EmptyCartException() throws Exception {
        when(orderService.createOrder("user123", null))
                .thenThrow(new EmptyCartException("User cart is empty"));

        mockMvc.perform(post("/api/orders/place")
                .param("userId", "user123"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User cart is empty"));

        verify(orderService).createOrder("user123", null);
    }

    @Test
    void testGetUserOrders() throws Exception {
        List<Order> orders = Arrays.asList(testOrder);
        when(orderService.getOrdersByUserId("user123")).thenReturn(orders);

        mockMvc.perform(get("/api/orders/user/user123"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value("order123"))
                .andExpect(jsonPath("$[0].userId").value("user123"));

        verify(orderService).getOrdersByUserId("user123");
    }

    @Test
    void testGetAllOrders() throws Exception {
        List<Order> orders = Arrays.asList(testOrder);
        when(orderService.getAllOrders()).thenReturn(orders);

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value("order123"));

        verify(orderService).getAllOrders();
    }

    @Test
    void testGetOrder_Found() throws Exception {
        when(orderService.getOrderById("order123")).thenReturn(Optional.of(testOrder));

        mockMvc.perform(get("/api/orders/order123"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("order123"));

        verify(orderService).getOrderById("order123");
    }

    @Test
    void testGetOrder_NotFound() throws Exception {
        when(orderService.getOrderById("nonexistent")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/orders/nonexistent"))
                .andExpect(status().isNotFound());

        verify(orderService).getOrderById("nonexistent");
    }
}
