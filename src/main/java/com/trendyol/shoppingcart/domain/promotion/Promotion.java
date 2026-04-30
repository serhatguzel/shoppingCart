package com.trendyol.shoppingcart.domain.promotion;

import com.trendyol.shoppingcart.domain.model.cart.Cart;

/**
 * Strategy interface — sealed so only 3 known promotion types exist.
 * TODO: Phase 4
 */
public sealed interface Promotion
        permits SameSellerPromotion, CategoryPromotion, TotalPricePromotion {

    int getPromotionId();

    boolean isApplicable(Cart cart);

    double calculateDiscount(Cart cart);
}
