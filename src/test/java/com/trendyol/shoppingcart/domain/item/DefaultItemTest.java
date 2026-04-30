package com.trendyol.shoppingcart.domain.item;

import org.junit.jupiter.api.Test;

import com.trendyol.shoppingcart.domain.exception.InvalidItemException;
import com.trendyol.shoppingcart.domain.model.item.DefaultItem;
import com.trendyol.shoppingcart.domain.model.item.VasItem;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;

public class DefaultItemTest {

    private DefaultItem furnitureItem;
    private DefaultItem electronicsItem;
    private DefaultItem otherItem;

    @BeforeEach
    void setup() {
        furnitureItem = new DefaultItem(1, 1001, 10, 100.0, 2);
        electronicsItem = new DefaultItem(2, 3004, 10, 200.0, 1);
        otherItem = new DefaultItem(3, 9999, 10, 50.0, 1);
    }

    @Test
    void addVasItem_shouldAddSuccessfully() {

        VasItem vasItem = new VasItem(100, 3242, 5003, 50.0, 1);
        furnitureItem.addVasItem(vasItem);

        assertThat(furnitureItem.getVasItems()).hasSize(1);
    }

    @Test
    void canAddVasItem_whenFurnitureCategory_shouldReturnTrue() {

        assertThat(furnitureItem.canAddVasItem()).isTrue();
    }

    @Test
    void canAddVasItem_whenElectronicsCategory_shouldReturnTrue() {

        assertThat(electronicsItem.canAddVasItem()).isTrue();
    }

    @Test
    void canAddVasItem_whenOtherCategory_shouldReturnFalse() {

        assertThat(otherItem.canAddVasItem()).isFalse();
    }

    @Test
    void getMaxQuantity_shouldReturn10() {

        assertThat(furnitureItem.getMaxQuantity()).isEqualTo(10);
    }

    @Test
    void getTotalPrice_shouldIncludeVasItemPrices() {

        VasItem vasItem = new VasItem(100, 3242, 5003, 50.0, 1);
        furnitureItem.addVasItem(vasItem);

        assertThat(furnitureItem.getTotalPrice()).isEqualTo(250.0);
    }

    @Test
    void getTotalPrice_shouldIncludeVasItemPricesWhenMultipleAdded() {

        VasItem vasItem1 = new VasItem(100, 3242, 5003, 50.0, 1);
        VasItem vasItem2 = new VasItem(101, 3242, 5003, 75.0, 2);
        furnitureItem.addVasItem(vasItem1);
        furnitureItem.addVasItem(vasItem2);

        assertThat(furnitureItem.getTotalPrice()).isEqualTo(400.0);
    }

    @Test
    void addVasItem_whenSameVasItemAdded_shouldIncreaseQuantity() {
        VasItem vasItem = new VasItem(100, 3242, 5003, 50.0, 1);

        furnitureItem.addVasItem(vasItem);
        furnitureItem.addVasItem(vasItem);

        assertThat(furnitureItem.getVasItems()).hasSize(1);

    }

    @Test
    void addVasItem_whenSameVasItemAddedTwice_shouldIncreaseQuantityByTwo() {

        VasItem vasItem = new VasItem(100, 3242, 5003, 50.0, 1);
        furnitureItem.addVasItem(vasItem);
        furnitureItem.addVasItem(vasItem);

        assertThat(furnitureItem.getVasItems().get(0).getQuantity()).isEqualTo(2);
    }

    @Test
    void addVasItem_whenVasItemLimitExceeded_shouldThrowException() {
        VasItem vasItem1 = new VasItem(100, 3242, 5003, 50.0, 1);
        VasItem vasItem2 = new VasItem(101, 3242, 5003, 75.0, 2);
        VasItem vasItem3 = new VasItem(102, 3242, 5003, 25.0, 1);
        VasItem vasItem4 = new VasItem(103, 3242, 5003, 100.0, 1);

        furnitureItem.addVasItem(vasItem1);
        furnitureItem.addVasItem(vasItem2);
        furnitureItem.addVasItem(vasItem3);
        assertThatThrownBy(() -> furnitureItem.addVasItem(vasItem4))
                .isInstanceOf(InvalidItemException.class);
    }

    @Test
    void addVasItem_whenItemNotEligible_shouldThrowException() {

        VasItem vasItem = new VasItem(100, 3242, 5003, 50.0, 1);

        assertThatThrownBy(() -> otherItem.addVasItem(vasItem))
                .isInstanceOf(InvalidItemException.class);
    }

    @Test
    void addVasItem_whenVasItemPriceHigherThanItem_shouldThrowException() {
        VasItem expensive = new VasItem(100, 3242, 5003, 999.0, 1);
        assertThatThrownBy(() -> furnitureItem.addVasItem(expensive))
                .isInstanceOf(InvalidItemException.class);
    }
}
