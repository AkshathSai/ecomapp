package com.hackathon.ecomapp.model;

import java.math.BigDecimal;

public class CartItem {
    private String productId;
    private String productName;
    private BigDecimal price;
    private Integer quantity;
    private Double carbonFootprint;

    public CartItem() {}

    public CartItem(String productId, String productName, BigDecimal price, Integer quantity, Double carbonFootprint) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.carbonFootprint = carbonFootprint;
    }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Double getCarbonFootprint() { return carbonFootprint; }
    public void setCarbonFootprint(Double carbonFootprint) { this.carbonFootprint = carbonFootprint; }

    public BigDecimal getTotalPrice() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }

    public Double getTotalCarbonFootprint() {
        return carbonFootprint * quantity;
    }
}
