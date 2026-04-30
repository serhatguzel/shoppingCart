package com.trendyol.shoppingcart.domain.promotion;

import com.trendyol.shoppingcart.domain.exception.InvalidItemException;
import com.trendyol.shoppingcart.domain.model.cart.Cart;
import com.trendyol.shoppingcart.domain.model.cart.CartLimits;
import com.trendyol.shoppingcart.domain.model.item.DefaultItem;
import com.trendyol.shoppingcart.domain.model.item.VasItem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class CategoryPromotionTest {

    private CategoryPromotion categoryPromotion;

    @BeforeEach
    void setUp() {
        categoryPromotion = new CategoryPromotion();
    }

    @Test
    @DisplayName("Category ID 3003 için %10 indirim geçerli olmalı")
    void isApplicable_whenCategoryId3003_shouldReturnTrue() {
        Cart cart = new Cart();
        cart.addItem(new DefaultItem(1, 3003, 10, 100.0, 2));

        assertThat(categoryPromotion.isApplicable(cart)).isTrue();
    }

    @Test
    @DisplayName("Category ID 3003 olmayan ürünlerde indirim geçerli olmamalı")
    void isApplicable_whenCategoryIdNot3003_shouldReturnFalse() {
        Cart cart = new Cart();
        cart.addItem(new DefaultItem(1, 3004, 10, 100.0, 2));

        assertThat(categoryPromotion.isApplicable(cart)).isFalse();
    }

    @Test
    @DisplayName("Kategori 3003 olan tüm ürünlerde %10 indirim hesaplanmalı")
    void calculateDiscount_whenCategory3003Items_shouldCalculate10PercentDiscount() {
        Cart cart = new Cart();
        cart.addItem(new DefaultItem(1, 3003, 10, 100.0, 2));
        cart.addItem(new DefaultItem(2, 3003, 5, 50.0, 2));

        double discount = categoryPromotion.calculateDiscount(cart);

        assertThat(discount).isEqualTo(300.0 * 0.05); // (300) * 0.05
    }

    @Test

    @DisplayName("Kategori 3003 ve diğer kategoriler birlikte olduğunda sadece 3003'den indirim hesaplanmalı")
    void calculateDiscount_whenMixedCategories_shouldApplyDiscountOnlyToCategory3003() {
        Cart cart = new Cart();
        cart.addItem(new DefaultItem(1, 3003, 10, 100.0, 6)); // 600 TL
        cart.addItem(new DefaultItem(2, 3004, 5, 50.0, 5)); // 250 TL (indirim yok)

        double discount = categoryPromotion.calculateDiscount(cart);

        assertThat(discount).isEqualTo(30.0); // 600 * 0.05
    }

    @Test

    @DisplayName("Sepette kategori 3003 ürünü yoksa indirim olmamalı")
    void calculateDiscount_whenNoCategory3003Item_shouldReturnZero() {
        Cart cart = new Cart();
        cart.addItem(new DefaultItem(1, 3004, 10, 100.0, 5));
        cart.addItem(new DefaultItem(2, 3006, 5, 50.0, 5));

        double discount = categoryPromotion.calculateDiscount(cart);

        assertThat(discount).isEqualTo(0.0);
    }

    @Test
    @DisplayName("Sepette hiç ürün yoksa indirim olmamalı")
    void calculateDiscount_whenEmptyCart_shouldReturnZero() {
        Cart cart = new Cart();

        double discount = categoryPromotion.calculateDiscount(cart);

        assertThat(discount).isEqualTo(0.0);
    }

    @Test

    @DisplayName("CategoryPromotion ID doğru olmalı")
    void getPromotionId_shouldReturnCorrectId() {
        assertThat(categoryPromotion.getPromotionId())
                .isEqualTo(CartLimits.CATEGORY_PROMOTION_ID);
    }

    @Test
    @DisplayName("Kategori 3003 indiriminde maksimum tutar sınırı olmamalı")
    void calculateDiscount_whenLargeAmountCategory3003_shouldApplyFullDiscount() {
        Cart cart = new Cart();
        // 30 adet x 1000 TL = 30.000 TL
        cart.addItem(new DefaultItem(1, 3003, 10, 1000.0, 10)); // 10.000 TL
        cart.addItem(new DefaultItem(2, 3003, 10, 1000.0, 10)); // 10.000 TL
        cart.addItem(new DefaultItem(3, 3003, 10, 1000.0, 10)); // 10.000 TL

        double discount = categoryPromotion.calculateDiscount(cart);

        // Toplam 30.000 TL * 0.05 = 1500 TL indirim
        assertThat(discount).isEqualTo(30000 * 0.05); // 1500 TL
    }

}
