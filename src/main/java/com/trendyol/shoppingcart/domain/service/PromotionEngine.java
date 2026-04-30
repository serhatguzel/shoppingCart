package com.trendyol.shoppingcart.domain.service;

import com.trendyol.shoppingcart.domain.model.cart.Cart;
import com.trendyol.shoppingcart.domain.promotion.CategoryPromotion;
import com.trendyol.shoppingcart.domain.promotion.Promotion;
import com.trendyol.shoppingcart.domain.promotion.SameSellerPromotion;
import com.trendyol.shoppingcart.domain.promotion.TotalPricePromotion;

import java.util.Optional;

/**
 * Selects the best (highest discount) applicable promotion for a cart.
 * TODO: Phase 4
 */
public class PromotionEngine {

    public Optional<AppliedPromotion> findBestPromotion(Cart cart) {

        Promotion categoryPromotion = new CategoryPromotion();
        Promotion sameSellerPromotion = new SameSellerPromotion();
        Promotion totalPricePromotion = new TotalPricePromotion();

        double categoryDiscount = categoryPromotion.calculateDiscount(cart);
        double sameSellerDiscount = sameSellerPromotion.calculateDiscount(cart);
        double totalPriceDiscount = totalPricePromotion.calculateDiscount(cart);

        double maxDiscount = Math.max(categoryDiscount, Math.max(sameSellerDiscount, totalPriceDiscount));

        if (maxDiscount > 0) {
            if (maxDiscount == categoryDiscount) {
                return Optional.of(new AppliedPromotion(categoryPromotion.getPromotionId(), categoryDiscount));
            } else if (maxDiscount == sameSellerDiscount) {
                return Optional.of(new AppliedPromotion(sameSellerPromotion.getPromotionId(), sameSellerDiscount));
            } else {
                return Optional.of(new AppliedPromotion(totalPricePromotion.getPromotionId(), totalPriceDiscount));
            }
        }

        return Optional.empty();
    }

    public record AppliedPromotion(int promotionId, double discount) {
    }
}
