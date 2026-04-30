package com.trendyol.shoppingcart.domain.item;

import org.junit.jupiter.api.Test;

import com.trendyol.shoppingcart.domain.exception.InvalidItemException;
import com.trendyol.shoppingcart.domain.model.item.DefaultItem;
import com.trendyol.shoppingcart.domain.model.item.DigitalItem;
import com.trendyol.shoppingcart.domain.model.item.Item;
import com.trendyol.shoppingcart.domain.model.item.ItemFactory;
import com.trendyol.shoppingcart.domain.model.item.VasItem;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ItemFactoryTest {

    @Test
    void create_shouldCreateDigitalItemWhenCategoryIdIs7889() {
        Item item = ItemFactory.create(1, 7889, 10, 100.0, 2);
        assertThat(item).isInstanceOf(DigitalItem.class);
    }

    @Test
    void create_shouldCreateDefaultItemWhenCategoryIdIsNot7889() {
        Item item = ItemFactory.create(2, 123, 20, 200.0, 1);
        assertThat(item).isInstanceOf(DefaultItem.class);
    }

    @Test
    void create_shouldThrowExceptionWhenSellerIdIs5003() {
        assertThatThrownBy(() -> ItemFactory.create(3, 456, 5003, 300.0, 3))
                .isInstanceOf(InvalidItemException.class);
    }

    @Test
    void createVasItem_shouldCreateVasItemWhenSellerIdIs5003AndCategoryIdIs3242() {
        VasItem vasItem = ItemFactory.createVasItem(4, 3242, 5003, 400.0, 4);
        assertThat(vasItem).isInstanceOf(VasItem.class);
    }

    @Test
    void createVasItem_shouldThrowExceptionWhenSellerIdIsNot5003() {
        assertThatThrownBy(() -> ItemFactory.createVasItem(5, 678, 700, 500.0, 5))
                .isInstanceOf(InvalidItemException.class);
    }

    @Test
    void createVasItem_shouldThrowExceptionWhenCategoryIdIsNot3242() {
        assertThatThrownBy(() -> ItemFactory.createVasItem(6, 890, 5003, 600.0, 6))
                .isInstanceOf(InvalidItemException.class);
    }

}
