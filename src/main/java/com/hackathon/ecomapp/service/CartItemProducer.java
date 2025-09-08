package com.hackathon.ecomapp.service;

import com.hackathon.ecomapp.event.CartItemEvent;
import com.hackathon.ecomapp.model.Cart;
import com.hackathon.ecomapp.model.Product;
import com.hackathon.ecomapp.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartItemProducer {

    final CartRepository cartRepository;
    final ProductService productService;
    final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String CART_TOPIC = "cart-items";

    public void addItemToCart(String userId, String productId, Integer quantity) {
        Optional<Product> productOpt = productService.getProductById(productId);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            CartItemEvent event = new CartItemEvent(
                userId, productId, product.getName(), product.getPrice(),
                quantity, product.getCarbonFootprint(), "ADD"
            );
            kafkaTemplate.send(CART_TOPIC, event);
            log.info("Published cart item event to Kafka: {}", event);
        }
    }

    public Optional<Cart> getCartByUserId(String userId) {
        return cartRepository.findByUserId(userId);
    }

    public void clearCart(String userId) {
        cartRepository.deleteByUserId(userId);
    }

    public void removeItemFromCart(String userId, String productId) {
        CartItemEvent event = new CartItemEvent(
            userId, productId, null, null, null, null, "REMOVE"
        );
        kafkaTemplate.send(CART_TOPIC, event);
        log.info("Published cart item removal event to Kafka: {}", event);
    }
}
