package com.trendyol.shoppingcart.infrastructure.json;

/**
 * Result of parsing one JSON input line.
 * payload is null for resetCart and displayCart.
 * TODO: Phase 6
 */
public record ParsedCommand(CommandType type, Object payload) {}
