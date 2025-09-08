package com.hackathon.ecomapp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "orders")
public class Order {
    @Id
    private String id;
    private String userId;
    private List<CartItem> items;
    private BigDecimal subtotal;
    private BigDecimal discount = BigDecimal.ZERO;
    private String promotionCode;
    private BigDecimal totalAmount;
    private Double totalCarbonFootprint;
    private OrderStatus status;
    private LocalDateTime orderDate;

    public enum OrderStatus {
        PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED
    }

    public Order() {
        this.orderDate = LocalDateTime.now();
        this.status = OrderStatus.PENDING;
    }

    public Order(String userId, List<CartItem> items) {
        this.userId = userId;
        this.items = items;
        this.orderDate = LocalDateTime.now();
        this.status = OrderStatus.PENDING;
        calculateTotals();
    }

    private void calculateTotals() {
        this.subtotal = items.stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        this.totalCarbonFootprint = items.stream()
                .mapToDouble(CartItem::getTotalCarbonFootprint)
                .sum();
        this.totalAmount = subtotal.subtract(discount);
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public List<CartItem> getItems() { return items; }
    public void setItems(List<CartItem> items) {
        this.items = items;
        calculateTotals();
    }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

    public BigDecimal getDiscount() { return discount; }
    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
        this.totalAmount = subtotal.subtract(discount);
    }

    public String getPromotionCode() { return promotionCode; }
    public void setPromotionCode(String promotionCode) { this.promotionCode = promotionCode; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public Double getTotalCarbonFootprint() { return totalCarbonFootprint; }
    public void setTotalCarbonFootprint(Double totalCarbonFootprint) { this.totalCarbonFootprint = totalCarbonFootprint; }

    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }

    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }
}
