package com.trendyol.shoppingcart.domain.model.item;

import com.trendyol.shoppingcart.domain.model.cart.CartLimits;

/**
 * Digital product (Steam card, donation card, etc.) — categoryId 7889.
 * Max quantity 5. Cannot hold VasItems.
 * TODO: Phase 2
 */
public class DigitalItem extends Item {

    public DigitalItem(int itemId, int categoryId, int sellerId, double price, int quantity) {
        super(itemId, categoryId, sellerId, price, quantity);
    }

    @Override
    public int getMaxQuantity() {
        return CartLimits.MAX_DIGITAL_ITEM_QUANTITY;
    }

    @Override
    public boolean canAddVasItem() {
        return false;
    }

}
