package com.hackathon.ecomapp.event;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class CartItemEvent {
    private String userId;
    private String productId;
    private String productName;
    private BigDecimal price;
    private Integer quantity;
    private Double carbonFootprint;
    private String action; // ADD, REMOVE, UPDATE

    public CartItemEvent(String userId, String productId, String productName,
                        BigDecimal price, Integer quantity, Double carbonFootprint, String action) {
        this.userId = userId;
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.carbonFootprint = carbonFootprint;
        this.action = action;
    }
}
