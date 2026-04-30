package com.trendyol.shoppingcart.application.dto.command;

/** addVasItemToItem command payload. TODO: Phase 5 */
public record AddVasItemCommand(
        int itemId,
        int vasItemId,
        int categoryId,
        int sellerId,
        double price,
        int quantity
) {}
