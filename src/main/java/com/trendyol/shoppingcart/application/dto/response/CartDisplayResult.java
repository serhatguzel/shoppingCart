package com.trendyol.shoppingcart.application.dto.response;

import java.util.List;

/**
 * displayCart command output payload.
 * TODO: Phase 5
 */
public record CartDisplayResult(
        List<ItemDisplay> items,
        double totalPrice,
        int appliedPromotionId,
        double totalDiscount
) {
    public record ItemDisplay(
            int itemId,
            int categoryId,
            int sellerId,
            double price,
            int quantity,
            List<VasItemDisplay> vasItems
    ) {}

    public record VasItemDisplay(
            int vasItemId,
            int categoryId,
            int sellerId,
            double price,
            int quantity
    ) {}
}
