package com.hackathon.ecomapp.service;

import com.hackathon.ecomapp.exception.EmptyCartException;
import com.hackathon.ecomapp.exception.InsufficientStockException;
import com.hackathon.ecomapp.exception.InvalidPromotionException;
import com.hackathon.ecomapp.model.*;
import com.hackathon.ecomapp.repository.OrderRepository;
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
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CartItemProducer cartItemProducer;

    @Mock
    private PromotionService promotionService;

    @Mock
    private ProductService productService;

    @Mock
    private Promotion testPromotion;

    @InjectMocks
    private OrderService orderService;

    private Cart testCart;
    private CartItem testItem;
    private Order testOrder;

    @BeforeEach
    void setUp() {
        testItem = new CartItem("prod123", "Laptop", new BigDecimal("1000.00"), 1, 5.0);
        testCart = new Cart("user123");
        testCart.addItem(testItem);

        testOrder = new Order("user123", testCart.getItems());
        testOrder.setId("order123");
    }

    @Test
    void testCreateOrder_Success() {
        when(cartItemProducer.getCartByUserId("user123")).thenReturn(Optional.of(testCart));
        when(productService.checkStockAvailability("prod123", 1)).thenReturn(true);
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        Order result = orderService.createOrder("user123", null);

        assertNotNull(result);
        verify(cartItemProducer).getCartByUserId("user123");
        verify(productService).checkStockAvailability("prod123", 1);
        verify(productService).updateStock("prod123", 1);
        verify(orderRepository).save(any(Order.class));
        verify(cartItemProducer).clearCart("user123");
    }

    @Test
    void testCreateOrder_EmptyCart() {
        when(cartItemProducer.getCartByUserId("user123")).thenReturn(Optional.empty());

        assertThrows(EmptyCartException.class,
                    () -> orderService.createOrder("user123", null));
        verify(cartItemProducer).getCartByUserId("user123");
        verify(orderRepository, never()).save(any());
    }

    @Test
    void testCreateOrder_CartWithNoItems() {
        Cart emptyCart = new Cart("user123");
        when(cartItemProducer.getCartByUserId("user123")).thenReturn(Optional.of(emptyCart));

        assertThrows(EmptyCartException.class,
                    () -> orderService.createOrder("user123", null));
        verify(cartItemProducer).getCartByUserId("user123");
        verify(orderRepository, never()).save(any());
    }

    @Test
    void testCreateOrder_InsufficientStock() {
        when(cartItemProducer.getCartByUserId("user123")).thenReturn(Optional.of(testCart));
        when(productService.checkStockAvailability("prod123", 1)).thenReturn(false);
        when(productService.getProductById("prod123")).thenReturn(Optional.of(new Product()));

        assertThrows(InsufficientStockException.class,
                    () -> orderService.createOrder("user123", null));
        verify(cartItemProducer).getCartByUserId("user123");
        verify(productService).checkStockAvailability("prod123", 1);
        verify(orderRepository, never()).save(any());
    }

    @Test
    void testCreateOrder_WithValidPromotion() {
        when(cartItemProducer.getCartByUserId("user123")).thenReturn(Optional.of(testCart));
        when(productService.checkStockAvailability("prod123", 1)).thenReturn(true);
        when(promotionService.validatePromotionCode("SAVE10", new BigDecimal("1000.00")))
                .thenReturn(Optional.of(testPromotion));
        when(testPromotion.getId()).thenReturn("promo123");
        when(testPromotion.calculateDiscount(new BigDecimal("1000.00"))).thenReturn(new BigDecimal("100.00"));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        Order result = orderService.createOrder("user123", "SAVE10");

        assertNotNull(result);
        verify(promotionService).validatePromotionCode("SAVE10", new BigDecimal("1000.00"));
        verify(promotionService).incrementUsageCount("promo123");
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void testCreateOrder_WithInvalidPromotion() {
        when(cartItemProducer.getCartByUserId("user123")).thenReturn(Optional.of(testCart));
        when(productService.checkStockAvailability("prod123", 1)).thenReturn(true);
        when(promotionService.validatePromotionCode(eq("INVALID"), any(BigDecimal.class)))
                .thenReturn(Optional.empty());

        assertThrows(InvalidPromotionException.class,
                    () -> orderService.createOrder("user123", "INVALID"));
        verify(promotionService).validatePromotionCode(eq("INVALID"), any(BigDecimal.class));
        verify(orderRepository, never()).save(any());
    }

    @Test
    void testGetOrdersByUserId() {
        List<Order> orders = Arrays.asList(testOrder);
        when(orderRepository.findByUserId("user123")).thenReturn(orders);

        List<Order> result = orderService.getOrdersByUserId("user123");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testOrder, result.get(0));
        verify(orderRepository).findByUserId("user123");
    }

    @Test
    void testGetOrderById_Found() {
        when(orderRepository.findById("order123")).thenReturn(Optional.of(testOrder));

        Optional<Order> result = orderService.getOrderById("order123");

        assertTrue(result.isPresent());
        assertEquals(testOrder, result.get());
        verify(orderRepository).findById("order123");
    }

    @Test
    void testGetOrderById_NotFound() {
        when(orderRepository.findById("nonexistent")).thenReturn(Optional.empty());

        Optional<Order> result = orderService.getOrderById("nonexistent");

        assertFalse(result.isPresent());
        verify(orderRepository).findById("nonexistent");
    }

    @Test
    void testGetAllOrders() {
        List<Order> orders = Arrays.asList(testOrder);
        when(orderRepository.findAll()).thenReturn(orders);

        List<Order> result = orderService.getAllOrders();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testOrder, result.get(0));
        verify(orderRepository).findAll();
    }
}
