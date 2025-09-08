package com.hackathon.ecomapp.service;

import com.hackathon.ecomapp.model.Promotion;
import com.hackathon.ecomapp.repository.PromotionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class PromotionServiceTest {

    @Mock
    private PromotionRepository promotionRepository;

    @InjectMocks
    private PromotionService promotionService;

    private Promotion validPromotion;
    private Promotion invalidPromotion;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();

        validPromotion = new Promotion("SAVE20", "20% discount",
                                     Promotion.DiscountType.PERCENTAGE,
                                     new BigDecimal("20"), new BigDecimal("100"),
                                     now.minusDays(1), now.plusDays(30));
        validPromotion.setId("promo123");
        validPromotion.setActive(true);
        validPromotion.setUsedCount(5);

        invalidPromotion = new Promotion("EXPIRED", "Expired discount",
                                        Promotion.DiscountType.PERCENTAGE,
                                        new BigDecimal("10"), new BigDecimal("50"),
                                        now.minusDays(30), now.minusDays(1));
        invalidPromotion.setId("promo456");
        invalidPromotion.setActive(true);
    }

    @Test
    void testValidatePromotionCode_ValidPromotion() {
        when(promotionRepository.findByCodeAndActiveTrue("SAVE20"))
                .thenReturn(Optional.of(validPromotion));

        Optional<Promotion> result = promotionService.validatePromotionCode("SAVE20", new BigDecimal("200"));

        assertTrue(result.isPresent());
        assertEquals(validPromotion, result.get());
        verify(promotionRepository).findByCodeAndActiveTrue("SAVE20");
    }

    @Test
    void testValidatePromotionCode_PromotionNotFound() {
        when(promotionRepository.findByCodeAndActiveTrue("NONEXISTENT"))
                .thenReturn(Optional.empty());

        Optional<Promotion> result = promotionService.validatePromotionCode("NONEXISTENT", new BigDecimal("200"));

        assertFalse(result.isPresent());
        verify(promotionRepository).findByCodeAndActiveTrue("NONEXISTENT");
    }

    @Test
    void testValidatePromotionCode_InvalidPromotion() {
        when(promotionRepository.findByCodeAndActiveTrue("EXPIRED"))
                .thenReturn(Optional.of(invalidPromotion));

        Optional<Promotion> result = promotionService.validatePromotionCode("EXPIRED", new BigDecimal("200"));

        assertFalse(result.isPresent());
        verify(promotionRepository).findByCodeAndActiveTrue("EXPIRED");
    }

    @Test
    void testValidatePromotionCode_InsufficientOrderAmount() {
        when(promotionRepository.findByCodeAndActiveTrue("SAVE20"))
                .thenReturn(Optional.of(validPromotion));

        Optional<Promotion> result = promotionService.validatePromotionCode("SAVE20", new BigDecimal("50"));

        assertFalse(result.isPresent());
        verify(promotionRepository).findByCodeAndActiveTrue("SAVE20");
    }

    @Test
    void testCalculateDiscount_ValidPromotion() {
        when(promotionRepository.findByCodeAndActiveTrue("SAVE20"))
                .thenReturn(Optional.of(validPromotion));

        BigDecimal discount = promotionService.calculateDiscount("SAVE20", new BigDecimal("200"));

        assertEquals(new BigDecimal("40"), discount);
        verify(promotionRepository).findByCodeAndActiveTrue("SAVE20");
    }

    @Test
    void testCalculateDiscount_InvalidPromotion() {
        when(promotionRepository.findByCodeAndActiveTrue("INVALID"))
                .thenReturn(Optional.empty());

        BigDecimal discount = promotionService.calculateDiscount("INVALID", new BigDecimal("200"));

        assertEquals(BigDecimal.ZERO, discount);
        verify(promotionRepository).findByCodeAndActiveTrue("INVALID");
    }

    @Test
    void testIncrementUsageCount_Success() {
        when(promotionRepository.findById("promo123")).thenReturn(Optional.of(validPromotion));
        when(promotionRepository.save(validPromotion)).thenReturn(validPromotion);

        promotionService.incrementUsageCount("promo123");

        assertEquals(6, validPromotion.getUsedCount()); // 5 + 1 = 6
        verify(promotionRepository).findById("promo123");
        verify(promotionRepository).save(validPromotion);
    }

    @Test
    void testIncrementUsageCount_PromotionNotFound() {
        when(promotionRepository.findById("nonexistent")).thenReturn(Optional.empty());

        promotionService.incrementUsageCount("nonexistent");

        verify(promotionRepository).findById("nonexistent");
        verify(promotionRepository, never()).save(any());
    }

    @Test
    void testCreatePromotion() {
        Promotion newPromotion = new Promotion("NEW10", "10% off",
                                             Promotion.DiscountType.PERCENTAGE,
                                             new BigDecimal("10"), new BigDecimal("50"),
                                             LocalDateTime.now(),
                                             LocalDateTime.now().plusDays(15));

        when(promotionRepository.save(newPromotion)).thenReturn(newPromotion);

        Promotion result = promotionService.createPromotion(newPromotion);

        assertNotNull(result);
        assertEquals(newPromotion, result);
        verify(promotionRepository).save(newPromotion);
    }

    @Test
    void testGetAllPromoCodes() {
        List<Promotion> promotions = Arrays.asList(validPromotion, invalidPromotion);
        when(promotionRepository.findAll()).thenReturn(promotions);

        List<Promotion> result = promotionService.getAllPromoCodes();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(promotions, result);
        verify(promotionRepository).findAll();
    }
}
