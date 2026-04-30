package com.trendyol.shoppingcart.application;

import com.trendyol.shoppingcart.application.dto.response.CommandResult;
import com.trendyol.shoppingcart.domain.model.cart.Cart;
import com.trendyol.shoppingcart.domain.service.PromotionEngine;
import com.trendyol.shoppingcart.infrastructure.json.ParsedCommand;

/**
 * Application Service — orchestrates use cases.
 * Delegates to domain, catches exceptions, returns CommandResult.
 * TODO: Phase 5
 */
public class CartApplicationService {

    private final Cart cart;
    private final PromotionEngine promotionEngine;

    public CartApplicationService(Cart cart, PromotionEngine promotionEngine) {
        this.cart            = cart;
        this.promotionEngine = promotionEngine;
    }

    public CommandResult execute(ParsedCommand command) {
        // TODO: Phase 5
        throw new UnsupportedOperationException("TODO: Phase 5");
    }
}
