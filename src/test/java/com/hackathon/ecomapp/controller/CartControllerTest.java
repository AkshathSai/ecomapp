package com.hackathon.ecomapp.controller;

import com.hackathon.ecomapp.model.Cart;
import com.hackathon.ecomapp.repository.CartRepository;
import com.hackathon.ecomapp.service.CartItemProducer;
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

@WebMvcTest(CartController.class)
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartRepository cartRepository;

    @MockBean
    private CartItemProducer cartItemProducer;

    private Cart testCart;

    @BeforeEach
    void setUp() {
        testCart = new Cart("user123");
        testCart.setId("cart123");
    }

    @Test
    void testGetCartItems() throws Exception {
        List<Cart> carts = Arrays.asList(testCart);
        when(cartRepository.findAll()).thenReturn(carts);

        mockMvc.perform(get("/api/cart"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value("cart123"))
                .andExpect(jsonPath("$[0].userId").value("user123"));

        verify(cartRepository).findAll();
    }

    @Test
    void testAddItemToCart() throws Exception {
        mockMvc.perform(post("/api/cart/add")
                .param("userId", "user123")
                .param("productId", "prod456")
                .param("quantity", "2"))
                .andExpect(status().isOk())
                .andExpect(content().string("Item added to cart asynchronously"));

        verify(cartItemProducer).addItemToCart("user123", "prod456", 2);
    }

    @Test
    void testRemoveItemFromCart() throws Exception {
        mockMvc.perform(delete("/api/cart/delete")
                .param("userId", "user123")
                .param("productId", "prod456"))
                .andExpect(status().isOk())
                .andExpect(content().string("Item removed from cart asynchronously"));

        verify(cartItemProducer).removeItemFromCart("user123", "prod456");
    }

    @Test
    void testGetCart_Found() throws Exception {
        when(cartItemProducer.getCartByUserId("user123")).thenReturn(Optional.of(testCart));

        mockMvc.perform(get("/api/cart/user123"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("cart123"))
                .andExpect(jsonPath("$.userId").value("user123"));

        verify(cartItemProducer).getCartByUserId("user123");
    }

    @Test
    void testGetCart_NotFound() throws Exception {
        when(cartItemProducer.getCartByUserId("nonexistent")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/cart/nonexistent"))
                .andExpect(status().isNotFound());

        verify(cartItemProducer).getCartByUserId("nonexistent");
    }

    @Test
    void testClearCart() throws Exception {
        mockMvc.perform(delete("/api/cart/user123"))
                .andExpect(status().isOk())
                .andExpect(content().string("Cart cleared successfully"));

        verify(cartItemProducer).clearCart("user123");
    }
}
