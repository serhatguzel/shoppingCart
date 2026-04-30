package com.trendyol.shoppingcart.domain.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.trendyol.shoppingcart.domain.model.cart.Cart;
import com.trendyol.shoppingcart.domain.model.cart.CartLimits;
import com.trendyol.shoppingcart.domain.model.item.DefaultItem;

public class PromotionEngineTest {

    PromotionEngine promotionEngine;

    @BeforeEach
    void setUp() {
        promotionEngine = new PromotionEngine();
    }

    @Test
    @DisplayName("Cart with multiple sellers should give Total Price Promotion discount")
    void findBestPromotion_whenCartHasMultipleSellers_shouldGiveTotalPricePromotionDiscount() {

        Cart cart = new Cart();
        // Farklı satıcılardan ürünler ekleyelim ki SameSellerPromotion devre dışı
        // kalsın!
        cart.addItem(new DefaultItem(1, 3003, 10, 1000.0, 3)); // Satıcı: 10
        cart.addItem(new DefaultItem(2, 3003, 20, 1000.0, 3)); // Satıcı: 20
        // Toplam sepet tutarı = 6000 TL

        // Kategori indirimi (3003) -> 6000 * %5 = 300 TL
        // Aynı Satıcı indirimi -> Farklı satıcılar var, o yüzden = 0 TL
        // Toplam Tutar indirimi -> 6000 TL, 5000-10000 dilimine girer = 500 TL
        // Beklenen en iyi indirim = 500 TL (TotalPricePromotion)

        assertThat(promotionEngine.findBestPromotion(cart).get().promotionId())
                .isEqualTo(CartLimits.TOTAL_PRICE_PROMOTION_ID);

        assertThat(promotionEngine.findBestPromotion(cart).get().discount())
                .isEqualTo(500.0);
    }

    @Test
    @DisplayName("Empty cart should not apply any promotion and return Optional.empty")
    void findBestPromotion_whenCartIsEmpty_shouldReturnEmpty() {
        Cart cart = new Cart();

        // Eğer sepet boşsa veya hiçbir indirim 0'dan büyük değilse boş (empty) dönmeli
        assertThat(promotionEngine.findBestPromotion(cart)).isEmpty();
    }

    @Test
    @DisplayName("Cart where category discount is higher should give Category Promotion discount")
    void findBestPromotion_whenCategoryDiscountIsHigher_shouldReturnCategoryPromotion() {
        Cart cart = new Cart();
        
        // SameSellerPromotion'ı bozmak için 2 farklı satıcıdan ürün ekliyoruz
        // İkisi de 3003 kategorisinde, toplam tutar 80.000 TL
        cart.addItem(new DefaultItem(1, 3003, 10, 40000.0, 1)); // Satıcı 10
        cart.addItem(new DefaultItem(2, 3003, 20, 40000.0, 1)); // Satıcı 20

        // Hesaplamalar:
        // Same Seller İndirimi = 0 TL (Satıcılar farklı)
        // Total Price İndirimi = 2000 TL (80.000 TL >= 50.000 TL dilimi)
        // Category İndirimi = 80.000 TL * %5 = 4000 TL

        // Beklenen kazanan: Category Promotion (4000 TL)
        assertThat(promotionEngine.findBestPromotion(cart).get().promotionId())
                .isEqualTo(CartLimits.CATEGORY_PROMOTION_ID);

        assertThat(promotionEngine.findBestPromotion(cart).get().discount())
                .isEqualTo(4000.0);
    }

}
