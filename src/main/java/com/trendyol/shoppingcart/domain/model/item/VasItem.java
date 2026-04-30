package com.trendyol.shoppingcart.domain.model.item;

/**
 * Value-Added Service item (insurance, assembly, etc.).
 * Only attachable to DefaultItem in Furniture/Electronics categories.
 * sellerId must be 5003, categoryId must be 3242.
 * TODO: Phase 2
 */
public class VasItem {

    private final int vasItemId;
    private final int categoryId;
    private final int sellerId;
    private final double price;
    private int quantity;

    public VasItem(int vasItemId, int categoryId, int sellerId, double price, int quantity) {
        this.vasItemId = vasItemId;
        this.categoryId = categoryId;
        this.sellerId = sellerId;
        this.price = price;
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return price * quantity;
    }

    public void increaseQuantity(int amount) {
        this.quantity += amount;
    }

    public int getVasItemId() {
        return vasItemId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public int getSellerId() {
        return sellerId;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }
}
