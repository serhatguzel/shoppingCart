package com.trendyol.shoppingcart.application.dto.response;

/**
 * Generic output for every command.
 * message can be a String (success/error text) or CartDisplayResult (displayCart).
 * TODO: Phase 5
 */
public record CommandResult(boolean result, Object message) {

    public static CommandResult success(String message) {
        return new CommandResult(true, message);
    }

    public static CommandResult success(Object payload) {
        return new CommandResult(true, payload);
    }

    public static CommandResult failure(String message) {
        return new CommandResult(false, message);
    }
}
