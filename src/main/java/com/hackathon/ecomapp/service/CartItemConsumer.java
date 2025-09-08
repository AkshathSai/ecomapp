package com.hackathon.ecomapp.service;

import com.hackathon.ecomapp.event.CartItemEvent;
import com.hackathon.ecomapp.model.Cart;
import com.hackathon.ecomapp.model.CartItem;
import com.hackathon.ecomapp.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartItemConsumer {

    final CartRepository cartRepository;

    @KafkaListener(topics = "cart-items", groupId = "ecom-group")
    public void handleCartItemEvent(CartItemEvent event) {
        log.info("Received cart item event: {}", event);
        switch (event.getAction()) {
            case "ADD":
                processAddItem(event);
                break;
            case "REMOVE":
                processRemoveItem(event);
                break;
            default:
                log.error("Unknown action: {}", event.getAction());
        }
    }

    private void processAddItem(CartItemEvent event) {
        Optional<Cart> cartOpt = cartRepository.findByUserId(event.getUserId());
        Cart cart = cartOpt.orElse(new Cart(event.getUserId()));

        CartItem cartItem = new CartItem(
                event.getProductId(), event.getProductName(),
                event.getPrice(), event.getQuantity(), event.getCarbonFootprint()
        );

        // Check if item already exists in cart
        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProductId().equals(event.getProductId()))
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(
                    existingItem.get().getQuantity() + event.getQuantity()
            );
        } else {
            cart.addItem(cartItem);
        }

        cartRepository.save(cart);
    }

    private void processRemoveItem(CartItemEvent event) {
        Optional<Cart> cartOpt = cartRepository.findByUserId(event.getUserId());
        if (cartOpt.isPresent()) {
            Cart cart = cartOpt.get();
            cart.getItems().removeIf(item -> item.getProductId().equals(event.getProductId()));
            cartRepository.save(cart);
        }
    }

}
