package com.trendyol.shoppingcart.domain.promotion;

import com.trendyol.shoppingcart.domain.model.cart.Cart;
import com.trendyol.shoppingcart.domain.model.item.DefaultItem;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SameSellerPromotionTest {

    private SameSellerPromotion ssp;

    @BeforeEach
    void setUp() {
        ssp = new SameSellerPromotion();
    }

    @Test
    @DisplayName("Aynı satıcıdan gelen ürünlerde %10 indirim hesaplanmalı")
    void calculateDiscount_whenSameSellerItems_shouldCalculate10PercentDiscount() {
        Cart cart = new Cart();
        cart.addItem(new DefaultItem(1, 3003, 10, 100.0, 2));
        cart.addItem(new DefaultItem(2, 3003, 10, 50.0, 2));

        double discount = ssp.calculateDiscount(cart);

        assertThat(discount).isEqualTo(300.0 * 0.10); // (300) * 0.10
    }

    @Test
    @DisplayName("Farklı satıcılardan gelen ürünlerde indirim hesaplanmamalı")
    void calculateDiscount_whenDifferentSellerItems_shouldCalculateNoDiscount() {
        Cart cart = new Cart();
        cart.addItem(new DefaultItem(1, 3003, 10, 100.0, 2));
        cart.addItem(new DefaultItem(2, 3003, 20, 50.0, 2));

        double discount = ssp.calculateDiscount(cart);

        assertThat(discount).isEqualTo(0.0);
    }

    @Test
    @DisplayName("Sepette hiç ürün yoksa indirim hesaplanmamalı")
    void calculateDiscount_whenEmptyCart_shouldCalculateNoDiscount() {
        Cart cart = new Cart();

        double discount = ssp.calculateDiscount(cart);

        assertThat(discount).isEqualTo(0.0);
    }

}
