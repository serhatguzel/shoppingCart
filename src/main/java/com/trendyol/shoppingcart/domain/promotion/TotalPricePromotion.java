package com.trendyol.shoppingcart.domain.promotion;

import com.trendyol.shoppingcart.domain.model.cart.Cart;

import static com.trendyol.shoppingcart.domain.model.cart.CartLimits.TOTAL_PRICE_PROMOTION_ID;

/**
 * ID 1232 — tiered fixed discount based on total cart price.
 * < 5000 → 250 TL | 5000-10000 → 500 TL | 10000-50000 → 1000 TL | ≥ 50000 →
 * 2000 TL
 * TODO: Phase 4
 */
public final class TotalPricePromotion implements Promotion {

    @Override
    public int getPromotionId() {
        return TOTAL_PRICE_PROMOTION_ID;
    }

    @Override
    public boolean isApplicable(Cart cart) {
        return cart.getTotalPrice() > 0;
    }

    @Override
    public double calculateDiscount(Cart cart) {

        if (cart.getTotalPrice() > 0 && cart.getTotalPrice() < 5000) {
            return 250;
        } else if (cart.getTotalPrice() > 5000 && cart.getTotalPrice() < 10000) {
            return 500;
        } else if (cart.getTotalPrice() > 10000 && cart.getTotalPrice() < 50000) {
            return 1000;
        } else if (cart.getTotalPrice() >= 50000) {
            return 2000;
        } else {
            return 0;
        }
    }
}
