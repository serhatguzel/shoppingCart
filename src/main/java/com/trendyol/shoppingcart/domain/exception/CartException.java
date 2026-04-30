package com.trendyol.shoppingcart.domain.exception;

/**
 * Base exception for all domain rule violations.
 * All business exceptions extend this class.
 */
public class CartException extends RuntimeException {
    public CartException(String message) {
        super(message);
    }
}
