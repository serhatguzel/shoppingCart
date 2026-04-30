package com.trendyol.shoppingcart.application.dto.command;

/** addItem command payload. TODO: Phase 5 */
public record AddItemCommand(
        int itemId,
        int categoryId,
        int sellerId,
        double price,
        int quantity
) {}
