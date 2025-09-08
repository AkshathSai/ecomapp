package com.hackathon.ecomapp.controller;

import com.hackathon.ecomapp.model.Promotion;
import com.hackathon.ecomapp.service.PromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/promotions")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class PromotionController {

    final PromotionService promotionService;

    @GetMapping
    public ResponseEntity<?> getAllPromotions() {
        return ResponseEntity.ok(promotionService.getAllPromoCodes());
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validatePromotion(@RequestParam String code,
                                              @RequestParam BigDecimal orderAmount) {
        return promotionService.validatePromotionCode(code, orderAmount)
                .map(promotion -> ResponseEntity.ok().body("Valid promotion: " + promotion.getDescription()))
                .orElse(ResponseEntity.badRequest().body("Invalid or expired promotion code"));
    }

    @PostMapping("/calculate-discount")
    public ResponseEntity<BigDecimal> calculateDiscount(@RequestParam String code,
                                                       @RequestParam BigDecimal orderAmount) {
        BigDecimal discount = promotionService.calculateDiscount(code, orderAmount);
        return ResponseEntity.ok(discount);
    }

    @PostMapping
    public ResponseEntity<Promotion> createPromotion(@RequestBody Promotion promotion) {
        return ResponseEntity.ok(promotionService.createPromotion(promotion));
    }
}
