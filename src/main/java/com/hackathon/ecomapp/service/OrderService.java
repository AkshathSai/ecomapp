package com.hackathon.ecomapp.service;

import com.hackathon.ecomapp.exception.EmptyCartException;
import com.hackathon.ecomapp.exception.InsufficientStockException;
import com.hackathon.ecomapp.exception.InvalidPromotionException;
import com.hackathon.ecomapp.model.Cart;
import com.hackathon.ecomapp.model.CartItem;
import com.hackathon.ecomapp.model.Order;
import com.hackathon.ecomapp.model.Promotion;
import com.hackathon.ecomapp.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartItemProducer cartItemProducer;

    @Autowired
    private PromotionService promotionService;

    @Autowired
    private ProductService productService;

    public Order createOrder(String userId, String promotionCode) {
        Optional<Cart> cartOpt = cartItemProducer.getCartByUserId(userId);
        if (cartOpt.isEmpty() || cartOpt.get().getItems().isEmpty()) {
            throw new EmptyCartException("User cart is empty");
        }

        Cart cart = cartOpt.get();

        // Validate stock availability
        for (CartItem item : cart.getItems()) {
            if (!productService.checkStockAvailability(item.getProductId(), item.getQuantity())) {
                throw new InsufficientStockException(item.getProductName(), item.getQuantity(),
                    productService.getProductById(item.getProductId())
                        .map(p -> p.getStock()).orElse(0));
            }
        }

        Order order = new Order(userId, cart.getItems());

        // Calculate carbon footprint: Footprint = sum(product.carbonFootprint * quantity)
        Double totalCarbonFootprint = cart.getItems().stream()
            .mapToDouble(item -> item.getCarbonFootprint() * item.getQuantity())
            .sum();
        order.setTotalCarbonFootprint(totalCarbonFootprint);

        // Apply promotion if provided
        if (promotionCode != null && !promotionCode.trim().isEmpty()) {
            Optional<Promotion> promotionOpt = promotionService.validatePromotionCode(promotionCode, order.getSubtotal());
            if (promotionOpt.isPresent()) {
                Promotion promotion = promotionOpt.get();
                BigDecimal discount = promotion.calculateDiscount(order.getSubtotal());
                order.setDiscount(discount);
                order.setPromotionCode(promotionCode);
                order.setTotalAmount(order.getSubtotal().subtract(discount));

                // Increment promotion usage
                promotionService.incrementUsageCount(promotion.getId());
            } else {
                throw InvalidPromotionException.forCode(promotionCode);
            }
        } else {
            order.setTotalAmount(order.getSubtotal());
        }

        // Update product stock
        for (CartItem item : cart.getItems()) {
            productService.updateStock(item.getProductId(), item.getQuantity());
        }

        // Save order and clear cart
        Order savedOrder = orderRepository.save(order);
        cartItemProducer.clearCart(userId);

        return savedOrder;
    }

    public List<Order> getOrdersByUserId(String userId) {
        return orderRepository.findByUserId(userId);
    }

    public Optional<Order> getOrderById(String orderId) {
        return orderRepository.findById(orderId);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
