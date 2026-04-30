package com.trendyol.shoppingcart.domain.model.item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.trendyol.shoppingcart.domain.exception.InvalidItemException;
import com.trendyol.shoppingcart.domain.model.cart.CartLimits;

/**
 * Standard e-commerce item (t-shirt, phone, etc.).
 * Can hold VasItems if category is Furniture or Electronics.
 * TODO: Phase 2
 */
public class DefaultItem extends Item {

    private final List<VasItem> vasItems;

    public DefaultItem(int itemId, int categoryId, int sellerId, double price, int quantity) {
        super(itemId, categoryId, sellerId, price, quantity);
        this.vasItems = new ArrayList<>();
    }

    @Override
    public int getMaxQuantity() {
        return CartLimits.MAX_ITEM_QUANTITY;
    }

    @Override
    public boolean canAddVasItem() {

        return (getCategoryId() == CartLimits.FURNITURE_CATEGORY_ID
                || getCategoryId() == CartLimits.ELECTRONICS_CATEGORY_ID);
    }

    @Override
    public double getTotalPrice() {
        return super.getTotalPrice() + vasItems.stream()
                .mapToDouble(VasItem::getTotalPrice)
                .sum();
    }

    public void addVasItem(VasItem vasItem) {
        if (!canAddVasItem()) {
            throw new InvalidItemException("VasItem cannot be added to this item.");
        }

        if (vasItems.size() >= CartLimits.MAX_VAS_ITEMS_PER_ITEM) {
            throw new InvalidItemException("VasItem cannot be added to this item.");
        }

        if (vasItem.getPrice() > getPrice()) {
            throw new InvalidItemException("VasItem price is too high.");
        }

        Optional<VasItem> existing = vasItems.stream()
                .filter(v -> v.getVasItemId() == vasItem.getVasItemId())
                .findFirst();

        if (existing.isPresent()) {
            existing.get().increaseQuantity(vasItem.getQuantity());
            return;
        }

        vasItems.add(vasItem);
    }

    public List<VasItem> getVasItems() {
        // Değiştirelemez nesne döndür
        return Collections.unmodifiableList(vasItems);
    }

}
