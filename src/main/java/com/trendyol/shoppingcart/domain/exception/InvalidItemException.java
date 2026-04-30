package com.trendyol.shoppingcart.domain.exception;

/** Thrown when an item has an invalid category or seller configuration. */
public class InvalidItemException extends CartException {
    public InvalidItemException(String message) {
        super(message);
    }
}
