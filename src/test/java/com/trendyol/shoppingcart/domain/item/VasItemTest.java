package com.trendyol.shoppingcart.domain.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.trendyol.shoppingcart.domain.model.item.VasItem;
import static org.assertj.core.api.Assertions.assertThat;

public class VasItemTest {
    private VasItem vasItem;

    @BeforeEach
    void setup() {
        vasItem = new VasItem(100, 3242, 5003, 50.0, 2);
    }

    @Test
    void getTotalPrice_shouldReturnPriceMultipliedByQuantity() {
        assertThat(vasItem.getTotalPrice()).isEqualTo(100.0);
    }

    @Test
    void increaseQuantity_shouldIncreaseQuantity() {
        vasItem.increaseQuantity(2);
        assertThat(vasItem.getQuantity()).isEqualTo(4);
    }
}
