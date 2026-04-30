package com.trendyol.shoppingcart.domain.promotion;

import com.trendyol.shoppingcart.domain.model.cart.Cart;
import com.trendyol.shoppingcart.domain.model.cart.CartLimits;
import com.trendyol.shoppingcart.domain.model.item.Item;

import static com.trendyol.shoppingcart.domain.model.cart.CartLimits.SAME_SELLER_PROMOTION_ID;

/**
 * ID 9909 — 10% discount when all non-VasItems share the same seller.
 * TODO: Phase 4
 */
public final class SameSellerPromotion implements Promotion {

    @Override
    public int getPromotionId() {
        return SAME_SELLER_PROMOTION_ID;
    }

    @Override
    public boolean isApplicable(Cart cart) {
        if (cart.getItems().isEmpty()) {
            return false;
        }
        // Sepetteki tüm ürünlerin satıcı ID'si aynı mı kontrol et (distinct count 1 ise
        // hepsi aynıdır)
        long distinctSellers = cart.getItems().stream()
                .map(Item::getSellerId)
                .distinct()
                .count();

        return distinctSellers == 1;
    }

    @Override
    public double calculateDiscount(Cart cart) {
        if (!isApplicable(cart)) {
            return 0.0;
        }

        // Promosyonlar VasItem fiyatlarına uygulanmaz!
        // Bu yüzden Item::getTotalPrice yerine sadece ürünün kendi fiyatı * adedi
        // hesaplanır.
        return cart.getItems().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum() * CartLimits.SAME_SELLER_PROMOTION_DISCOUNT;
    }
}
