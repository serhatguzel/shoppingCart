package com.trendyol.shoppingcart.domain.exception;

/** Thrown when adding an item exceeds a cart-level limit (unique items, quantity, amount). */
public class CartLimitExceededException extends CartException {
    public CartLimitExceededException(String message) {
        super(message);
    }
}
