package com.trendyol.shoppingcart.domain.model.cart;

import com.trendyol.shoppingcart.domain.exception.InvalidItemException;
import com.trendyol.shoppingcart.domain.model.item.DefaultItem;
import com.trendyol.shoppingcart.domain.model.item.DigitalItem;
import com.trendyol.shoppingcart.domain.model.item.Item;
import com.trendyol.shoppingcart.domain.model.item.VasItem;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class Cart {

    private List<Item> items;

    public Cart() {
        this.items = new ArrayList<>();
    }

    public List<Item> getItems() {
        return Collections.unmodifiableList(items);
    }

    public double getTotalPrice() {
        return items.stream().mapToDouble(Item::getTotalPrice).sum();
    }

    public int getTotalItemCount() {
        return items.stream().mapToInt(Item::getQuantity).sum();
    }

    public void addItem(Item item) {

        if (getTotalItemCount() + item.getQuantity() > CartLimits.MAX_TOTAL_QUANTITY) {
            throw new InvalidItemException(
                    "Sepete en fazla " + CartLimits.MAX_TOTAL_QUANTITY + " ürün eklenebilir.");
        }

        if (getTotalPrice() + item.getTotalPrice() > CartLimits.MAX_CART_AMOUNT) {
            throw new InvalidItemException("Sepet tutarı " + CartLimits.MAX_CART_AMOUNT + " TL'yi geçemez.");
        }

        if (item instanceof DigitalItem digitalItem) {
            if (digitalItem.getQuantity() > CartLimits.MAX_DIGITAL_ITEM_QUANTITY)
                throw new InvalidItemException(
                        "Sepette en fazla " + CartLimits.MAX_DIGITAL_ITEM_QUANTITY + " dijital ürün eklenebilir.");
        }

        if (item instanceof DefaultItem defaultItem) {
            if (defaultItem.getQuantity() > CartLimits.MAX_ITEM_QUANTITY)
                throw new InvalidItemException(
                        "Sepette en fazla " + CartLimits.MAX_ITEM_QUANTITY + " default ürün eklenebilir.");
        }

        Item existingItem = items.stream()
                .filter(i -> i.getItemId() == item.getItemId())
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            existingItem.increaseQuantity(item.getQuantity());
        } else {
            if (items.size() >= CartLimits.MAX_UNIQUE_ITEMS) {
                throw new InvalidItemException(
                        "Sepette en fazla " + CartLimits.MAX_UNIQUE_ITEMS + " farklı ürün olabilir.");
            }

            items.add(item);
        }

    }

    public void addVasItemToItem(int itemId, VasItem vasItem) {
        Item existingItem = items.stream()
                .filter(i -> i.getItemId() == itemId)
                .findFirst()
                .orElseThrow(() -> new InvalidItemException("Sepette böyle bir ürün yok."));

        if (existingItem instanceof DefaultItem defaultItem) {
            if (!defaultItem.canAddVasItem()) {
                throw new InvalidItemException("Bu ürüne vas eklenemez.");
            }
            if (getTotalPrice() + vasItem.getTotalPrice() > CartLimits.MAX_CART_AMOUNT) {
                throw new InvalidItemException("Sepet tutarı " + CartLimits.MAX_CART_AMOUNT + " TL'yi geçemez.");
            }
            defaultItem.addVasItem(vasItem);
        } else {
            throw new InvalidItemException("Bu ürüne vas eklenemez.");
        }
    }

    public void removeItem(int itemId) {
        boolean isRemoved = items.removeIf(item -> item.getItemId() == itemId);
        if (!isRemoved) {
            throw new InvalidItemException("Sepette silinecek böyle bir ürün bulunamadı.");
        }
    }

    public void reset() {
        items.clear();
    }
}
