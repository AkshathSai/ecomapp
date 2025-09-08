package com.hackathon.ecomapp.service;

import com.hackathon.ecomapp.model.Promotion;
import com.hackathon.ecomapp.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PromotionService {

    final PromotionRepository promotionRepository;

    public Optional<Promotion> validatePromotionCode(String code, BigDecimal orderAmount) {
        Optional<Promotion> promotionOpt = promotionRepository.findByCodeAndActiveTrue(code);

        if (promotionOpt.isPresent()) {
            Promotion promotion = promotionOpt.get();
            if (promotion.isValid(orderAmount)) {
                return Optional.of(promotion);
            }
        }
        return Optional.empty();
    }

    public BigDecimal calculateDiscount(String code, BigDecimal orderAmount) {
        Optional<Promotion> promotionOpt = validatePromotionCode(code, orderAmount);
        return promotionOpt.map(promotion -> promotion.calculateDiscount(orderAmount))
                          .orElse(BigDecimal.ZERO);
    }

    public void incrementUsageCount(String promotionId) {
        Optional<Promotion> promotionOpt = promotionRepository.findById(promotionId);
        if (promotionOpt.isPresent()) {
            Promotion promotion = promotionOpt.get();
            promotion.setUsedCount(promotion.getUsedCount() + 1);
            promotionRepository.save(promotion);
        }
    }

    public Promotion createPromotion(Promotion promotion) {
        return promotionRepository.save(promotion);
    }

    public List<Promotion> getAllPromoCodes() {
        return promotionRepository.findAll();
    }
}
