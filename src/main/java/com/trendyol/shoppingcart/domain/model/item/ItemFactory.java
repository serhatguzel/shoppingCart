package com.trendyol.shoppingcart.domain.model.item;

import com.trendyol.shoppingcart.domain.model.cart.CartLimits;
import com.trendyol.shoppingcart.domain.exception.InvalidItemException;

/**
 * Factory Method pattern — creates the correct Item subtype.
 * Decision: categoryId 7889 → DigitalItem, otherwise → DefaultItem.
 * sellerId 5003 → exception (VasItem cannot be added standalone).
 * TODO: Phase 2
 */
public final class ItemFactory {

    private ItemFactory() {
    }

    public static Item create(int itemId, int categoryId, int sellerId, double price, int quantity) {

        if (sellerId == CartLimits.VAS_ITEM_SELLER_ID) {
            throw new InvalidItemException("VasItem cannot be added to this item.");
        }

        if (categoryId == CartLimits.DIGITAL_ITEM_CATEGORY_ID) {
            return new DigitalItem(itemId, categoryId, sellerId, price, quantity);
        } else {
            return new DefaultItem(itemId, categoryId, sellerId, price, quantity);
        }
    }

    public static VasItem createVasItem(int vasItemId, int categoryId, int sellerId, double price, int quantity) {
        if (sellerId != CartLimits.VAS_ITEM_SELLER_ID) {
            throw new InvalidItemException("VasItem cannot be added to this item.");
        }

        if (categoryId == CartLimits.VAS_ITEM_CATEGORY_ID) {
            return new VasItem(vasItemId, categoryId, sellerId, price, quantity);
        } else {
            throw new InvalidItemException("VasItem cannot be added to this item.");
        }
    }
}
