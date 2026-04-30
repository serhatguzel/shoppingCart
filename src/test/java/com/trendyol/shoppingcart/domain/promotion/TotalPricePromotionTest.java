package com.trendyol.shoppingcart.domain.promotion;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.trendyol.shoppingcart.domain.model.cart.Cart;
import com.trendyol.shoppingcart.domain.model.item.DefaultItem;

public class TotalPricePromotionTest {

    TotalPricePromotion totalPricePromotion;

    @BeforeEach
    void setUp() {
        totalPricePromotion = new TotalPricePromotion();
    }

    @Test
    @DisplayName("Total price less than 5000 TL should give 250 TL discount")
    void calculateDiscount_whenTotalPriceLessThan5000_shouldGive250TLDiscount() {
        Cart cart = new Cart();
        cart.addItem(new DefaultItem(1, 3003, 10, 100.0, 2));
        cart.addItem(new DefaultItem(2, 3003, 20, 50.0, 2));
        double discount = totalPricePromotion.calculateDiscount(cart);
        assertThat(discount).isEqualTo(250.0);
    }

    @Test
    @DisplayName("Total price between 5000 and 10000 TL should give 500 TL discount")
    void calculateDiscount_whenTotalPriceBetween5000And10000_shouldGive500TLDiscount() {
        Cart cart = new Cart();
        cart.addItem(new DefaultItem(1, 3003, 10, 3000.0, 2));
        cart.addItem(new DefaultItem(2, 3003, 20, 200.0, 1));
        double discount = totalPricePromotion.calculateDiscount(cart);
        assertThat(discount).isEqualTo(500.0);
    }

    @Test
    @DisplayName("Total price between 10000 and 50000 TL should give 1000 TL discount")
    void calculateDiscount_whenTotalPriceBetween10000And50000_shouldGive1000TLDiscount() {
        Cart cart = new Cart();
        cart.addItem(new DefaultItem(1, 3003, 10, 8000.0, 1));
        cart.addItem(new DefaultItem(2, 3003, 20, 3000.0, 1));
        double discount = totalPricePromotion.calculateDiscount(cart);
        assertThat(discount).isEqualTo(1000.0);
    }

    @Test
    @DisplayName("Total price greater than or equal to 50000 TL should give 2000 TL discount")
    void calculateDiscount_whenTotalPriceGreaterThanOrEqualTo50000_shouldGive2000TLDiscount() {
        Cart cart = new Cart();
        cart.addItem(new DefaultItem(1, 3003, 10, 45000.0, 1));
        cart.addItem(new DefaultItem(2, 3003, 20, 6000.0, 1));
        double discount = totalPricePromotion.calculateDiscount(cart);
        assertThat(discount).isEqualTo(2000.0);
    }

    @Test
    @DisplayName("Empty cart should give 0 TL discount")
    void calculateDiscount_whenEmptyCart_shouldGive0TLDiscount() {
        Cart cart = new Cart();
        double discount = totalPricePromotion.calculateDiscount(cart);
        assertThat(discount).isEqualTo(0.0);
    }

}
