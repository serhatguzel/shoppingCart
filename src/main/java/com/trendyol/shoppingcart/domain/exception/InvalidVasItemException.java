package com.trendyol.shoppingcart.domain.exception;

/** Thrown when a VasItem violates attachment rules (wrong seller, category, price or limit). */
public class InvalidVasItemException extends CartException {
    public InvalidVasItemException(String message) {
        super(message);
    }
}
