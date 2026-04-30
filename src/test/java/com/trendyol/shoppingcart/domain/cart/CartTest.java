package com.trendyol.shoppingcart.domain.cart;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.trendyol.shoppingcart.domain.exception.InvalidItemException;
import com.trendyol.shoppingcart.domain.model.cart.Cart;
import com.trendyol.shoppingcart.domain.model.item.DefaultItem;
import com.trendyol.shoppingcart.domain.model.item.DigitalItem;
import com.trendyol.shoppingcart.domain.model.item.Item;
import com.trendyol.shoppingcart.domain.model.item.VasItem;

public class CartTest {

    @Test
    void addItem_whenValidItem_shouldAddToCart() {
        Cart cart = new Cart();
        Item item = new DefaultItem(1, 1001, 10, 100.0, 2);
        cart.addItem(item);
        assertThat(cart.getTotalItemCount()).isEqualTo(2);
    }

    @Test
    void addItem_whenSameItemAddedTwice_shouldIncreaseQuantity() {
        Cart cart = new Cart();
        Item item1 = new DefaultItem(1, 1001, 10, 100.0, 2);
        Item item2 = new DefaultItem(1, 1001, 10, 100.0, 3);

        cart.addItem(item1);
        cart.addItem(item2);

        assertThat(cart.getItems()).hasSize(1);
        assertThat(cart.getTotalItemCount()).isEqualTo(5);
    }

    @Test
    void addItem_whenAddedMoreThanMax10UniqueItems_shouldThrowInvalidItemException() throws InvalidItemException {
        Cart cart = new Cart();

        for (int i = 1; i <= 10; i++) {
            cart.addItem(new DefaultItem(i, 1001, 10, 100.0, 1));
        }

        Item item11 = new DefaultItem(11, 1001, 10, 100.0, 1);
        assertThatThrownBy(() -> cart.addItem(item11))
                .isInstanceOf(InvalidItemException.class);
    }

    @Test
    void addItem_whenAddedMoreThanMax30Items_shouldThrowInvalidItemException() throws InvalidItemException {
        Cart cart = new Cart();

        for (int i = 1; i <= 10; i++) {
            cart.addItem(new DefaultItem(i, 1001, 30, 100.0, 3));
        }

        Item item31 = new DefaultItem(31, 1001, 10, 100.0, 1);
        assertThatThrownBy(() -> cart.addItem(item31))
                .isInstanceOf(InvalidItemException.class);
    }

    @Test
    void addItem_whenAddedMoreThanMax500000Price_shouldThrowInvalidItemException() throws InvalidItemException {
        Cart cart = new Cart();

        cart.addItem(new DefaultItem(1, 1001, 10, 100000.0, 3));
        Item item2 = new DefaultItem(2, 1002, 10, 100000.0, 3);

        assertThatThrownBy(() -> cart.addItem(item2))
                .isInstanceOf(InvalidItemException.class);
    }

    @Test
    void addItem_whenAddedMoreThanMax10DefaultItem_shouldThrowInvalidItemException() throws InvalidItemException {
        Cart cart = new Cart();

        Item item = new DefaultItem(1, 1001, 10, 1000.0, 11);

        assertThatThrownBy(() -> cart.addItem(item))
                .isInstanceOf(InvalidItemException.class);
    }

    @Test
    void addItem_whenAddedMoreThanMax5DigitalItem_shouldThrowInvalidItemException() throws InvalidItemException {
        Cart cart = new Cart();

        Item item = new DigitalItem(1, 7889, 10, 10000.0, 6);

        assertThatThrownBy(() -> cart.addItem(item))
                .isInstanceOf(InvalidItemException.class);
    }

    @Test
    void addVasItemToItem_whenValidVasItem_shouldIncreaseCartTotalPrice() throws InvalidItemException {
        Cart cart = new Cart();

        Item defaultItem = new DefaultItem(1, 3004, 10, 100.0, 1);
        cart.addItem(defaultItem);
        // Olası bir VasItem oluştur (Fiyat: 20 TL)
        VasItem vasItem = new VasItem(101, 3242, 5003, 20.0, 1);

        // Sepetteki 1 ID'li ürüne VasItem'ı ekle
        cart.addVasItemToItem(1, vasItem);
        // Toplam sepet tutarı 100 + 20 = 120 TL olmalı
        assertThat(cart.getTotalPrice()).isEqualTo(120.0);
    }

    @Test
    void addVasItemToItem_whenItemNotFound_shouldThrowInvalidItemException() {
        Cart cart = new Cart();
        VasItem vasItem = new VasItem(101, 3242, 5003, 20.0, 1);

        // Sepet boşken 999 ID'li (olmayan) bir ürüne eklemeye çalış
        assertThatThrownBy(() -> cart.addVasItemToItem(999, vasItem))
                .isInstanceOf(InvalidItemException.class)
                .hasMessageContaining("Sepette böyle bir ürün yok");
    }

    @Test
    void addVasItemToItem_whenItemIsDigital_shouldThrowInvalidItemException() throws InvalidItemException {
        Cart cart = new Cart();

        // 7889 kategorili bir dijital ürün ekle
        Item digitalItem = new DigitalItem(2, 7889, 10, 100.0, 1);
        cart.addItem(digitalItem);

        VasItem vasItem = new VasItem(101, 3242, 5003, 20.0, 1);

        // Dijital ürüne VasItem eklemeye çalış
        assertThatThrownBy(() -> cart.addVasItemToItem(2, vasItem))
                .isInstanceOf(InvalidItemException.class)
                .hasMessageContaining("Bu ürüne vas eklenemez");
    }

    @Test
    void addVasItemToItem_whenExceedsMaxCartAmount_shouldThrowInvalidItemException() throws InvalidItemException {
        Cart cart = new Cart();

        // Sınıra çok yakın bir fiyatta ürün ekle (490.000 TL)
        Item defaultItem = new DefaultItem(1, 3004, 10, 490000.0, 1);
        cart.addItem(defaultItem);

        // Sepeti 500.000 sınırının üstüne çıkaracak pahalı bir VasItem ekle (20.000 TL)
        VasItem vasItem = new VasItem(101, 3242, 5003, 20000.0, 1);

        // VasItem eklendiğinde toplam 510.000 TL olacağı için patlamalı
        assertThatThrownBy(() -> cart.addVasItemToItem(1, vasItem))
                .isInstanceOf(InvalidItemException.class)
                .hasMessageContaining("TL'yi geçemez");
    }

    @Test
    void removeItem_whenValidItem_shouldRemoveFromCart() {
        Cart cart = new Cart();
        Item item = new DefaultItem(1, 1001, 10, 100.0, 2);
        cart.addItem(item);

        cart.removeItem(item.getItemId());

        assertThat(cart.getTotalItemCount()).isEqualTo(0);
    }

    @Test
    void removeItem_whenItemNotFound_shouldThrowInvalidItemException() {
        Cart cart = new Cart();
        Item item = new DefaultItem(1, 1001, 10, 100.0, 2);

        assertThatThrownBy(() -> cart.removeItem(item.getItemId()))
                .isInstanceOf(InvalidItemException.class);
    }

    @Test
    void removeItem_whenMultipleItemsInCart_shouldOnlyRemoveSpecifiedItem() {
        Cart cart = new Cart();
        Item item1 = new DefaultItem(1, 1001, 10, 100.0, 2);
        Item item2 = new DefaultItem(2, 1002, 10, 50.0, 1);

        cart.addItem(item1);
        cart.addItem(item2);

        cart.removeItem(item1.getItemId());

        assertThat(cart.getItems()).hasSize(1);
        assertThat(cart.getItems().get(0).getItemId()).isEqualTo(2);
        assertThat(cart.getTotalItemCount()).isEqualTo(1);
    }

    @Test
    void reset_shouldClearAllItemsFromCart() {
        Cart cart = new Cart();
        Item item1 = new DefaultItem(1, 1001, 10, 100.0, 2);
        cart.addItem(item1);

        cart.reset();

        assertThat(cart.getItems()).isEmpty();
        assertThat(cart.getTotalItemCount()).isEqualTo(0);
        assertThat(cart.getTotalPrice()).isEqualTo(0.0);
    }

}
