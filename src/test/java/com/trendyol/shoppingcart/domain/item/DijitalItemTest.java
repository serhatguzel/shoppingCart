package com.trendyol.shoppingcart.domain.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import com.trendyol.shoppingcart.domain.exception.InvalidItemException;
import com.trendyol.shoppingcart.domain.model.item.DigitalItem;

public class DijitalItemTest {

    private DigitalItem digitalItem;

    @BeforeEach
    void setup() {
        digitalItem = new DigitalItem(1, 7889, 10, 100.0, 2);

    }

    @Test
    void getMaxQuantity_shouldReturn5() {

        assertThat(digitalItem.getMaxQuantity()).isEqualTo(5);
    }

    @Test
    void getTotalPrice_shouldReturnPriceMultipliedByQuantity() {
        assertThat(digitalItem.getTotalPrice()).isEqualTo(200.0);
    }

    @Test
    void canAddVasItem_shouldReturnFalse() {
        assertThat(digitalItem.canAddVasItem()).isFalse();
    }

    @Test
    void increaseQuantity_shouldIncreaseQuantity() {
        digitalItem.increaseQuantity(3); // Başlangıçta 2 adet var. 3 daha eklendi, toplam 5 oldu (Sınır 5)
        assertThat(digitalItem.getQuantity()).isEqualTo(5);
    }

    @Test
    void increaseQuantity_whenExceedsLimit_shouldThrowException() {
        // Sınır 5 olduğu için 4 tane daha (Toplam 6) eklemeye çalışırsak hata fırlatmalı
        assertThatThrownBy(() -> digitalItem.increaseQuantity(4))
                .isInstanceOf(InvalidItemException.class)
                .hasMessageContaining("en fazla 5 adet olabilir");
    }

    @Test
    void constructor_whenPriceIsZero_shouldThrowException() {
        assertThatThrownBy(() -> new DigitalItem(1, 7889, 10, 0.0, 1))
                .isInstanceOf(InvalidItemException.class);
    }

    @Test
    void constructor_whenQuantityIsNegative_shouldThrowException() {
        assertThatThrownBy(() -> new DigitalItem(1, 7889, 10, 100.0, -1))
                .isInstanceOf(InvalidItemException.class);
    }

}

// DigitalItem max quantity 5
// DefaultItem max quantity 10
// VasItem sadece sellerId=5003 ile oluşturulabilir
// ItemFactory, sellerId=5003 olan item'ı direkt oluşturamaz
// ItemFactory, categoryId=7889 için DigitalItem döner
// DefaultItem sadece Furniture/Electronics kategorisinde VasItem kabul eder
