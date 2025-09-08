package com.hackathon.ecomapp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document(collection = "promotions")
public class Promotion {
    @Id
    private String id;
    private String code;
    private String description;
    private DiscountType discountType;
    private BigDecimal discountValue;
    private BigDecimal minimumOrderAmount;
    private LocalDateTime validFrom;
    private LocalDateTime validTo;
    private boolean active;
    private Integer usageLimit;
    private Integer usedCount = 0;

    public enum DiscountType {
        PERCENTAGE, FIXED_AMOUNT
    }

    public Promotion() {}

    public Promotion(String code, String description, DiscountType discountType,
                    BigDecimal discountValue, BigDecimal minimumOrderAmount,
                    LocalDateTime validFrom, LocalDateTime validTo) {
        this.code = code;
        this.description = description;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.minimumOrderAmount = minimumOrderAmount;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.active = true;
    }

    public boolean isValid(BigDecimal orderAmount) {
        LocalDateTime now = LocalDateTime.now();
        return active &&
               now.isAfter(validFrom) &&
               now.isBefore(validTo) &&
               orderAmount.compareTo(minimumOrderAmount) >= 0 &&
               (usageLimit == null || usedCount < usageLimit);
    }

    public BigDecimal calculateDiscount(BigDecimal orderAmount) {
        if (!isValid(orderAmount)) {
            return BigDecimal.ZERO;
        }

        if (discountType == DiscountType.PERCENTAGE) {
            return orderAmount.multiply(discountValue).divide(BigDecimal.valueOf(100));
        } else {
            return discountValue;
        }
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public DiscountType getDiscountType() { return discountType; }
    public void setDiscountType(DiscountType discountType) { this.discountType = discountType; }

    public BigDecimal getDiscountValue() { return discountValue; }
    public void setDiscountValue(BigDecimal discountValue) { this.discountValue = discountValue; }

    public BigDecimal getMinimumOrderAmount() { return minimumOrderAmount; }
    public void setMinimumOrderAmount(BigDecimal minimumOrderAmount) { this.minimumOrderAmount = minimumOrderAmount; }

    public LocalDateTime getValidFrom() { return validFrom; }
    public void setValidFrom(LocalDateTime validFrom) { this.validFrom = validFrom; }

    public LocalDateTime getValidTo() { return validTo; }
    public void setValidTo(LocalDateTime validTo) { this.validTo = validTo; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public Integer getUsageLimit() { return usageLimit; }
    public void setUsageLimit(Integer usageLimit) { this.usageLimit = usageLimit; }

    public Integer getUsedCount() { return usedCount; }
    public void setUsedCount(Integer usedCount) { this.usedCount = usedCount; }
}
