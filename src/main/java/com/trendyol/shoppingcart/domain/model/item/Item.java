package com.trendyol.shoppingcart.domain.model.item;

import com.trendyol.shoppingcart.domain.exception.InvalidItemException;

/**
 * Sepete bağımsız olarak eklenebilen tüm ürün tiplerinin soyut tabanı.
 *
 * <p>
 * <b>Template Method Pattern:</b> Ortak state (price, quantity, getter'lar)
 * ve ortak davranış (getTotalPrice, increaseQuantity) burada tanımlanır.
 * Alt sınıflar sadece kendilerine özgü kuralları uygular:
 * <ul>
 * <li>{@link #getMaxQuantity()} — her tip için maksimum adet farklıdır</li>
 * <li>{@link #canAddVasItem()} — sadece belirli DefaultItem kategorileri kabul
 * eder</li>
 * </ul>
 * </p>
 *
 * <p>
 * <b>Önemli:</b> {@link com.trendyol.shoppingcart.domain.model.item.VasItem}
 * bu hiyerarşinin dışındadır — VasItem bağımsız bir sepet ürünü değil,
 * DefaultItem'a bağlı bir hizmet kalemidir.
 * </p>
 */
public abstract class Item {

    protected final int itemId;
    protected final int categoryId;
    protected final int sellerId;
    protected final double price;
    protected int quantity;

    protected Item(int itemId, int categoryId, int sellerId, double price, int quantity) {
        validate(price, quantity);
        this.itemId = itemId;
        this.categoryId = categoryId;
        this.sellerId = sellerId;
        this.price = price;
        this.quantity = quantity;
    }

    // ── Template Methods ──────────────────────────────────────────────────────

    /** Bu item tipine eklenebilecek maksimum adet. */
    public abstract int getMaxQuantity();

    /** Bu item'a VasItem eklenip eklenemeyeceği. */
    public abstract boolean canAddVasItem();

    /**
     * Bu item'ın toplam fiyatı (birim fiyat × adet).
     * DefaultItem, VasItem toplamını dahil etmek için override eder.
     */
    public double getTotalPrice() {
        return price * quantity;
    }

    /**
     * Sepete aynı item tekrar eklendiğinde adedi artırır.
     * Maksimum adet kontrolü Cart tarafından çağrılmadan önce yapılır.
     */
    public void increaseQuantity(int amount) {
        this.quantity += amount;
    }

    // ── Getters ───────────────────────────────────────────────────────────────

    public int getItemId() {
        return itemId;
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

    // ── Private ───────────────────────────────────────────────────────────────

    private static void validate(double price, int quantity) {
        if (price <= 0) {
            throw new InvalidItemException("Item price must be greater than 0.");
        }
        if (quantity <= 0) {
            throw new InvalidItemException("Item quantity must be greater than 0.");
        }
    }
}
