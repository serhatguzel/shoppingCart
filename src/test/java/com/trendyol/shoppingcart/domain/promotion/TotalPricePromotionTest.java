package com.trendyol.shoppingcart.domain.promotion;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.trendyol.shoppingcart.domain.model.cart.Cart;
import com.trendyol.shoppingcart.domain.model.item.DefaultItem;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class TotalPricePromotionTest {

    TotalPricePromotion totalPricePromotion;

    @BeforeEach
    void setUp() {
        totalPricePromotion = new TotalPricePromotion();
    }

    @ParameterizedTest(name = "Sepet tutarı {0} TL iken, {1} TL indirim uygulanmalı")
    @CsvSource({
            "300.0, 250.0",     // 5000'den küçük
            "6200.0, 500.0",    // 5000 - 10000 arası
            "11000.0, 1000.0",  // 10000 - 50000 arası
            "51000.0, 2000.0",  // 50000 ve üzeri
            "0.0, 0.0"          // Sepet boş
    })
    void calculateDiscount_withDifferentTotalPrices_shouldGiveCorrectDiscount(double cartTotal, double expectedDiscount) {
        Cart cart = new Cart();
        
        if (cartTotal > 0) {
            // İstenen sepet tutarını elde etmek için tek bir ürün ekliyoruz
            cart.addItem(new DefaultItem(1, 3003, 10, cartTotal, 1));
        }

        double discount = totalPricePromotion.calculateDiscount(cart);

        assertThat(discount).isEqualTo(expectedDiscount);
    }

}
