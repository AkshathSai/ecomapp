package com.hackathon.ecomapp.service;

import com.hackathon.ecomapp.exception.InsufficientStockException;
import com.hackathon.ecomapp.exception.ResourceNotFoundException;
import com.hackathon.ecomapp.model.Product;
import com.hackathon.ecomapp.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product testProduct;

    @BeforeEach
    void setUp() {
        testProduct = new Product("Laptop", "Gaming laptop",
                                new BigDecimal("999.99"), 10, 5.2);
        testProduct.setId("prod123");
    }

    @Test
    void testGetAllProducts() {
        List<Product> products = Arrays.asList(testProduct);
        when(productRepository.findAll()).thenReturn(products);

        List<Product> result = productService.getAllProducts();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testProduct, result.get(0));
        verify(productRepository).findAll();
    }

    @Test
    void testGetProductById_Found() {
        when(productRepository.findById("prod123")).thenReturn(Optional.of(testProduct));

        Optional<Product> result = productService.getProductById("prod123");

        assertTrue(result.isPresent());
        assertEquals(testProduct, result.get());
        verify(productRepository).findById("prod123");
    }

    @Test
    void testGetProductById_NotFound() {
        when(productRepository.findById("nonexistent")).thenReturn(Optional.empty());

        Optional<Product> result = productService.getProductById("nonexistent");

        assertFalse(result.isPresent());
        verify(productRepository).findById("nonexistent");
    }

    @Test
    void testGetProductByIdRequired_Found() {
        when(productRepository.findById("prod123")).thenReturn(Optional.of(testProduct));

        Product result = productService.getProductByIdRequired("prod123");

        assertNotNull(result);
        assertEquals(testProduct, result);
        verify(productRepository).findById("prod123");
    }

    @Test
    void testGetProductByIdRequired_NotFound() {
        when(productRepository.findById("nonexistent")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                    () -> productService.getProductByIdRequired("nonexistent"));
        verify(productRepository).findById("nonexistent");
    }

    @Test
    void testCreateProduct() {
        when(productRepository.save(testProduct)).thenReturn(testProduct);

        Product result = productService.createProduct(testProduct);

        assertNotNull(result);
        assertEquals(testProduct, result);
        verify(productRepository).save(testProduct);
    }

    @Test
    void testCheckStockAvailability_Available() {
        when(productRepository.findById("prod123")).thenReturn(Optional.of(testProduct));

        boolean result = productService.checkStockAvailability("prod123", 5);

        assertTrue(result);
        verify(productRepository).findById("prod123");
    }

    @Test
    void testCheckStockAvailability_InsufficientStock() {
        when(productRepository.findById("prod123")).thenReturn(Optional.of(testProduct));

        boolean result = productService.checkStockAvailability("prod123", 15);

        assertFalse(result);
        verify(productRepository).findById("prod123");
    }

    @Test
    void testCheckStockAvailability_ProductNotFound() {
        when(productRepository.findById("nonexistent")).thenReturn(Optional.empty());

        boolean result = productService.checkStockAvailability("nonexistent", 5);

        assertFalse(result);
        verify(productRepository).findById("nonexistent");
    }

    @Test
    void testUpdateStock_Success() {
        when(productRepository.findById("prod123")).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        productService.updateStock("prod123", 3);

        assertEquals(7, testProduct.getStock()); // 10 - 3 = 7
        verify(productRepository).findById("prod123");
        verify(productRepository).save(testProduct);
    }

    @Test
    void testUpdateStock_InsufficientStock() {
        when(productRepository.findById("prod123")).thenReturn(Optional.of(testProduct));

        assertThrows(InsufficientStockException.class,
                    () -> productService.updateStock("prod123", 15));
        verify(productRepository).findById("prod123");
        verify(productRepository, never()).save(any());
    }

    @Test
    void testUpdateStock_ProductNotFound() {
        when(productRepository.findById("nonexistent")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                    () -> productService.updateStock("nonexistent", 5));
        verify(productRepository).findById("nonexistent");
        verify(productRepository, never()).save(any());
    }
}
