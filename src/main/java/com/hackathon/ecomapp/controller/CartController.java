package com.hackathon.ecomapp.controller;

import com.hackathon.ecomapp.model.Cart;
import com.hackathon.ecomapp.repository.CartRepository;
import com.hackathon.ecomapp.service.CartItemProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class CartController {

    final CartRepository cartRepository;
    final CartItemProducer cartItemProducer;

    @GetMapping()
    public ResponseEntity<List<Cart>> getCartItems() {
        return ResponseEntity.ok(cartRepository.findAll());
    }

    @PostMapping("/add")
    public ResponseEntity<String> addItemToCart(@RequestParam String userId,
                                               @RequestParam String productId,
                                               @RequestParam Integer quantity) {
        cartItemProducer.addItemToCart(userId, productId, quantity);
        return ResponseEntity.ok("Item added to cart asynchronously");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> removeItemFromCart(@RequestParam String userId,
                                                     @RequestParam String productId) {
        cartItemProducer.removeItemFromCart(userId, productId);
        return ResponseEntity.ok("Item removed from cart asynchronously");
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Cart> getCart(@PathVariable String userId) {
        Optional<Cart> cart = cartItemProducer.getCartByUserId(userId);
        return cart.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> clearCart(@PathVariable String userId) {
        cartItemProducer.clearCart(userId);
        return ResponseEntity.ok("Cart cleared successfully");
    }
}
