package com.hackathon.ecomapp.controller;

import com.hackathon.ecomapp.model.Promotion;
import com.hackathon.ecomapp.service.PromotionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@WebMvcTest(PromotionController.class)
class PromotionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PromotionService promotionService;

    @Autowired
    private ObjectMapper objectMapper;

    private Promotion testPromotion;

    @BeforeEach
    void setUp() {
        testPromotion = new Promotion("SAVE20", "20% discount",
                                    Promotion.DiscountType.PERCENTAGE,
                                    new BigDecimal("20"), new BigDecimal("100"),
                                    LocalDateTime.now().minusDays(1),
                                    LocalDateTime.now().plusDays(30));
        testPromotion.setId("promo123");
    }

    @Test
    void testGetAllPromotions() throws Exception {
        List<Promotion> promotions = Arrays.asList(testPromotion);
        when(promotionService.getAllPromoCodes()).thenReturn(promotions);

        mockMvc.perform(get("/api/promotions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value("promo123"))
                .andExpect(jsonPath("$[0].code").value("SAVE20"));

        verify(promotionService).getAllPromoCodes();
    }

    @Test
    void testValidatePromotion_Valid() throws Exception {
        when(promotionService.validatePromotionCode("SAVE20", new BigDecimal("200")))
                .thenReturn(Optional.of(testPromotion));

        mockMvc.perform(post("/api/promotions/validate")
                .param("code", "SAVE20")
                .param("orderAmount", "200"))
                .andExpect(status().isOk())
                .andExpect(content().string("Valid promotion: 20% discount"));

        verify(promotionService).validatePromotionCode("SAVE20", new BigDecimal("200"));
    }

    @Test
    void testValidatePromotion_Invalid() throws Exception {
        when(promotionService.validatePromotionCode("INVALID", new BigDecimal("200")))
                .thenReturn(Optional.empty());

        mockMvc.perform(post("/api/promotions/validate")
                .param("code", "INVALID")
                .param("orderAmount", "200"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid or expired promotion code"));

        verify(promotionService).validatePromotionCode("INVALID", new BigDecimal("200"));
    }

    @Test
    void testCalculateDiscount() throws Exception {
        when(promotionService.calculateDiscount("SAVE20", new BigDecimal("200")))
                .thenReturn(new BigDecimal("40.00"));

        mockMvc.perform(post("/api/promotions/calculate-discount")
                .param("code", "SAVE20")
                .param("orderAmount", "200"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("40.00"));

        verify(promotionService).calculateDiscount("SAVE20", new BigDecimal("200"));
    }

    @Test
    void testCreatePromotion() throws Exception {
        Promotion newPromotion = new Promotion("NEW10", "10% off",
                                             Promotion.DiscountType.PERCENTAGE,
                                             new BigDecimal("10"), new BigDecimal("50"),
                                             LocalDateTime.now(),
                                             LocalDateTime.now().plusDays(15));

        Promotion savedPromotion = new Promotion("NEW10", "10% off",
                                               Promotion.DiscountType.PERCENTAGE,
                                               new BigDecimal("10"), new BigDecimal("50"),
                                               LocalDateTime.now(),
                                               LocalDateTime.now().plusDays(15));
        savedPromotion.setId("promo456");

        when(promotionService.createPromotion(any(Promotion.class))).thenReturn(savedPromotion);

        mockMvc.perform(post("/api/promotions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newPromotion)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("promo456"))
                .andExpect(jsonPath("$.code").value("NEW10"));

        verify(promotionService).createPromotion(any(Promotion.class));
    }
}
