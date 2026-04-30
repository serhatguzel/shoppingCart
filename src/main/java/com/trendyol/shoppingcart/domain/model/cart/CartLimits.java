package com.trendyol.shoppingcart.domain.model.cart;

/**
 * Tüm iş kuralı sabitlerinin tek merkezi.
 *
 * Sepet limitleri, kategori/satıcı ID'leri ve promosyon ID'leri burada
 * tanımlanır.
 * Hiçbir yerden doğrudan sayı (magic number) kullanılmaz — her zaman bu
 * sabitler referans alınır.
 */
public final class CartLimits {

    public static final int MAX_UNIQUE_ITEMS = 10;
    public static final int MAX_TOTAL_QUANTITY = 30;
    public static final double MAX_CART_AMOUNT = 500_000.0;
    public static final int MAX_ITEM_QUANTITY = 10;
    public static final int MAX_DIGITAL_ITEM_QUANTITY = 5;
    public static final int MAX_VAS_ITEMS_PER_ITEM = 3;

    public static final int DIGITAL_ITEM_CATEGORY_ID = 7889;
    public static final int VAS_ITEM_CATEGORY_ID = 3242;
    public static final int FURNITURE_CATEGORY_ID = 1001;
    public static final int ELECTRONICS_CATEGORY_ID = 3004;
    public static final int CATEGORY_PROMOTION_CAT_ID = 3003;

    public static final int VAS_ITEM_SELLER_ID = 5003;

    public static final int SAME_SELLER_PROMOTION_ID = 9909;
    public static final int CATEGORY_PROMOTION_ID = 5676;
    public static final int TOTAL_PRICE_PROMOTION_ID = 1232;
    public static final double CATEGORY_PROMOTION_DISCOUNT = 0.05;
    public static final double SAME_SELLER_PROMOTION_DISCOUNT = 0.10;

    private CartLimits() {
    }
}
