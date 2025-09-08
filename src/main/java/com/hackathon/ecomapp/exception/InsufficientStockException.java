package com.hackathon.ecomapp.exception;

public class InsufficientStockException extends RuntimeException {

    public InsufficientStockException(String productName, Integer requested, Integer available) {
        super(String.format("Insufficient stock for product '%s'. Requested: %d, Available: %d",
                productName, requested, available));
    }
}
