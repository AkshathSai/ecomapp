package com.hackathon.ecomapp.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

class PromotionTest {

    private Promotion promotion;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();
        promotion = new Promotion();
    }

    @Test
    void testDefaultConstructor() {
        assertNotNull(promotion);
        assertNull(promotion.getId());
        assertNull(promotion.getCode());
        assertEquals(0, promotion.getUsedCount());
    }

    @Test
    void testParameterizedConstructor() {
        LocalDateTime validFrom = now.minusDays(1);
        LocalDateTime validTo = now.plusDays(30);

        Promotion promotion = new Promotion("SAVE20", "20% discount",
                                          Promotion.DiscountType.PERCENTAGE,
                                          new BigDecimal("20"), new BigDecimal("100"),
                                          validFrom, validTo);

        assertEquals("SAVE20", promotion.getCode());
        assertEquals("20% discount", promotion.getDescription());
        assertEquals(Promotion.DiscountType.PERCENTAGE, promotion.getDiscountType());
        assertEquals(new BigDecimal("20"), promotion.getDiscountValue());
        assertEquals(new BigDecimal("100"), promotion.getMinimumOrderAmount());
        assertEquals(validFrom, promotion.getValidFrom());
        assertEquals(validTo, promotion.getValidTo());
        assertTrue(promotion.isActive());
    }

    @Test
    void testSettersAndGetters() {
        promotion.setId("promo123");
        promotion.setCode("DISCOUNT10");
        promotion.setDescription("10% off");
        promotion.setDiscountType(Promotion.DiscountType.FIXED_AMOUNT);
        promotion.setDiscountValue(new BigDecimal("50"));
        promotion.setMinimumOrderAmount(new BigDecimal("200"));
        promotion.setActive(false);
        promotion.setUsageLimit(100);
        promotion.setUsedCount(25);

        assertEquals("promo123", promotion.getId());
        assertEquals("DISCOUNT10", promotion.getCode());
        assertEquals("10% off", promotion.getDescription());
        assertEquals(Promotion.DiscountType.FIXED_AMOUNT, promotion.getDiscountType());
        assertEquals(new BigDecimal("50"), promotion.getDiscountValue());
        assertEquals(new BigDecimal("200"), promotion.getMinimumOrderAmount());
        assertFalse(promotion.isActive());
        assertEquals(100, promotion.getUsageLimit());
        assertEquals(25, promotion.getUsedCount());
    }

    @Test
    void testIsValid_ValidPromotion() {
        setupValidPromotion();

        assertTrue(promotion.isValid(new BigDecimal("150")));
    }

    @Test
    void testIsValid_InactivePromotion() {
        setupValidPromotion();
        promotion.setActive(false);

        assertFalse(promotion.isValid(new BigDecimal("150")));
    }

    @Test
    void testIsValid_ExpiredPromotion() {
        setupValidPromotion();
        promotion.setValidTo(now.minusDays(1));

        assertFalse(promotion.isValid(new BigDecimal("150")));
    }

    @Test
    void testIsValid_NotYetValidPromotion() {
        setupValidPromotion();
        promotion.setValidFrom(now.plusDays(1));

        assertFalse(promotion.isValid(new BigDecimal("150")));
    }

    @Test
    void testIsValid_InsufficientOrderAmount() {
        setupValidPromotion();

        assertFalse(promotion.isValid(new BigDecimal("50")));
    }

    @Test
    void testIsValid_UsageLimitReached() {
        setupValidPromotion();
        promotion.setUsageLimit(5);
        promotion.setUsedCount(5);

        assertFalse(promotion.isValid(new BigDecimal("150")));
    }

    @Test
    void testIsValid_WithinUsageLimit() {
        setupValidPromotion();
        promotion.setUsageLimit(10);
        promotion.setUsedCount(5);

        assertTrue(promotion.isValid(new BigDecimal("150")));
    }

    @Test
    void testCalculateDiscount_PercentageDiscount() {
        setupValidPromotion();
        promotion.setDiscountType(Promotion.DiscountType.PERCENTAGE);
        promotion.setDiscountValue(new BigDecimal("20"));

        BigDecimal discount = promotion.calculateDiscount(new BigDecimal("200"));
        assertEquals(new BigDecimal("40"), discount);
    }

    @Test
    void testCalculateDiscount_FixedAmountDiscount() {
        setupValidPromotion();
        promotion.setDiscountType(Promotion.DiscountType.FIXED_AMOUNT);
        promotion.setDiscountValue(new BigDecimal("50"));

        BigDecimal discount = promotion.calculateDiscount(new BigDecimal("200"));
        assertEquals(new BigDecimal("50"), discount);
    }

    @Test
    void testCalculateDiscount_InvalidPromotion() {
        setupValidPromotion();
        promotion.setActive(false);

        BigDecimal discount = promotion.calculateDiscount(new BigDecimal("200"));
        assertEquals(BigDecimal.ZERO, discount);
    }

    @Test
    void testDiscountTypes() {
        assertEquals("PERCENTAGE", Promotion.DiscountType.PERCENTAGE.toString());
        assertEquals("FIXED_AMOUNT", Promotion.DiscountType.FIXED_AMOUNT.toString());
    }

    private void setupValidPromotion() {
        promotion.setActive(true);
        promotion.setValidFrom(now.minusDays(1));
        promotion.setValidTo(now.plusDays(30));
        promotion.setMinimumOrderAmount(new BigDecimal("100"));
        promotion.setDiscountType(Promotion.DiscountType.PERCENTAGE);
        promotion.setDiscountValue(new BigDecimal("10"));
    }
}
