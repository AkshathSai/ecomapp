package com.hackathon.ecomapp.exception;

public class InvalidPromotionException extends RuntimeException {
    public InvalidPromotionException(String message) {
        super(message);
    }

    public static InvalidPromotionException forCode(String promotionCode) {
        return new InvalidPromotionException(String.format("Invalid or expired promotion code: '%s'", promotionCode));
    }
}
