package com.hackathon.ecomapp.controller;

import com.hackathon.ecomapp.model.Product;
import com.hackathon.ecomapp.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private Product testProduct;

    @BeforeEach
    void setUp() {
        testProduct = new Product("Laptop", "Gaming laptop",
                                new BigDecimal("999.99"), 10, 5.2);
        testProduct.setId("prod123");
    }

    @Test
    void testGetAllProducts() throws Exception {
        List<Product> products = Arrays.asList(testProduct);
        when(productService.getAllProducts()).thenReturn(products);

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value("prod123"))
                .andExpect(jsonPath("$[0].name").value("Laptop"))
                .andExpect(jsonPath("$[0].price").value(999.99));

        verify(productService).getAllProducts();
    }

    @Test
    void testGetProductById_Success() throws Exception {
        when(productService.getProductByIdRequired("prod123")).thenReturn(testProduct);

        mockMvc.perform(get("/api/products/prod123"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("prod123"))
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.description").value("Gaming laptop"));

        verify(productService).getProductByIdRequired("prod123");
    }

    @Test
    void testCreateProduct() throws Exception {
        Product newProduct = new Product("Smartphone", "Latest phone",
                                       new BigDecimal("699.99"), 20, 3.0);
        Product savedProduct = new Product("Smartphone", "Latest phone",
                                         new BigDecimal("699.99"), 20, 3.0);
        savedProduct.setId("prod456");

        when(productService.createProduct(any(Product.class))).thenReturn(savedProduct);

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newProduct)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("prod456"))
                .andExpect(jsonPath("$.name").value("Smartphone"));

        verify(productService).createProduct(any(Product.class));
    }
}
