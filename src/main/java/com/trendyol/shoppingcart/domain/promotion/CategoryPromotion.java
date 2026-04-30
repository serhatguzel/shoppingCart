package com.trendyol.shoppingcart.domain.promotion;

import com.trendyol.shoppingcart.domain.model.cart.Cart;
import com.trendyol.shoppingcart.domain.model.cart.CartLimits;
import com.trendyol.shoppingcart.domain.model.item.Item;

import static com.trendyol.shoppingcart.domain.model.cart.CartLimits.CATEGORY_PROMOTION_ID;

public final class CategoryPromotion implements Promotion {

    @Override
    public int getPromotionId() {
        return CATEGORY_PROMOTION_ID;
    }

    @Override
    public boolean isApplicable(Cart cart) {
        return cart.getItems().stream()
                .anyMatch(item -> item.getCategoryId() == CartLimits.CATEGORY_PROMOTION_CAT_ID);

    }

    @Override
    public double calculateDiscount(Cart cart) {
        return cart.getItems().stream()
                .filter(item -> item.getCategoryId() == CartLimits.CATEGORY_PROMOTION_CAT_ID)
                .mapToDouble(Item::getTotalPrice)
                .sum() * CartLimits.CATEGORY_PROMOTION_DISCOUNT;
    }
}
