package com.trendyol.shoppingcart.application;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.trendyol.shoppingcart.application.dto.command.AddItemCommand;
import com.trendyol.shoppingcart.application.dto.command.AddVasItemCommand;
import com.trendyol.shoppingcart.application.dto.command.RemoveItemCommand;
import com.trendyol.shoppingcart.application.dto.response.CartDisplayResult;
import com.trendyol.shoppingcart.application.dto.response.CommandResult;
import com.trendyol.shoppingcart.domain.model.cart.Cart;
import com.trendyol.shoppingcart.domain.model.item.ItemFactory;
import com.trendyol.shoppingcart.domain.service.PromotionEngine;
import com.trendyol.shoppingcart.infrastructure.json.CommandType;
import com.trendyol.shoppingcart.infrastructure.json.ParsedCommand;

public class CartApplicationServiceTest {

    private CartApplicationService applicationService;
    private Cart cart;

    @BeforeEach
    void setUp() {
        cart = new Cart();
        PromotionEngine promotionEngine = new PromotionEngine();
        applicationService = new CartApplicationService(cart, promotionEngine);
    }

    @Test
    @DisplayName("Should add item to cart and return success result")
    void execute_whenAddItemCommand_shouldReturnSuccess() {
        AddItemCommand commandPayload = new AddItemCommand(1, 3004, 10, 100.0, 1);
        ParsedCommand command = new ParsedCommand(CommandType.addItem, commandPayload);

        CommandResult result = applicationService.execute(command);

        assertThat(result.result()).isTrue();
        assertThat(result.message()).isEqualTo("Item added successfully");
        assertThat(cart.getItems()).hasSize(1);
    }

    @Test
    @DisplayName("Should catch CartException and return failure result when business rule is violated")
    void execute_whenDomainThrowsException_shouldReturnFailure() {
        // Negatif fiyat -> Domain hatası
        AddItemCommand commandPayload = new AddItemCommand(1, 3004, 10, -50.0, 1);
        ParsedCommand command = new ParsedCommand(CommandType.addItem, commandPayload);

        CommandResult result = applicationService.execute(command);

        assertThat(result.result()).isFalse();
        assertThat(result.message()).asString().contains("greater than 0");
        assertThat(cart.getItems()).isEmpty();
    }

    @Test
    @DisplayName("Should add VasItem and return success")
    void execute_whenAddVasItemCommand_shouldReturnSuccess() {
        // Önce geçerli bir ana ürün ekle (Mobilya kategorisi)
        cart.addItem(ItemFactory.create(1, 1001, 10, 500.0, 1));
        
        // Sonra o ürüne VasItem ekle
        AddVasItemCommand vasCommandPayload = new AddVasItemCommand(1, 2, 3242, 5003, 50.0, 1);
        ParsedCommand command = new ParsedCommand(CommandType.addVasItemToItem, vasCommandPayload);

        CommandResult result = applicationService.execute(command);

        assertThat(result.result()).isTrue();
        assertThat(result.message()).isEqualTo("VasItem added successfully");
    }

    @Test
    @DisplayName("Should remove item and return success")
    void execute_whenRemoveItemCommand_shouldReturnSuccess() {
        cart.addItem(ItemFactory.create(1, 3004, 10, 100.0, 1));
        
        RemoveItemCommand removeCommandPayload = new RemoveItemCommand(1);
        ParsedCommand command = new ParsedCommand(CommandType.removeItem, removeCommandPayload);

        CommandResult result = applicationService.execute(command);

        assertThat(result.result()).isTrue();
        assertThat(result.message()).isEqualTo("Item removed successfully");
        assertThat(cart.getItems()).isEmpty();
    }

    @Test
    @DisplayName("Should reset cart and return success")
    void execute_whenResetCartCommand_shouldReturnSuccess() {
        cart.addItem(ItemFactory.create(1, 3004, 10, 100.0, 1));
        
        ParsedCommand command = new ParsedCommand(CommandType.resetCart, null);
        CommandResult result = applicationService.execute(command);

        assertThat(result.result()).isTrue();
        assertThat(result.message()).isEqualTo("Cart reset successfully");
        assertThat(cart.getItems()).isEmpty();
    }

    @Test
    @DisplayName("Should return failure for unknown command type")
    void execute_whenUnknownCommand_shouldReturnFailure() {
        // Normalde ParsedCommand enum alıyor ama eğer bir şekilde null vs gelirse diye test
        ParsedCommand command = new ParsedCommand(null, null);

        CommandResult result = applicationService.execute(command);

        assertThat(result.result()).isFalse();
        assertThat(result.message()).asString().contains("Unexpected error");
    }

    @Test
    @DisplayName("Should handle displayCart command correctly including VasItems and Digital Items")
    void execute_whenDisplayCartCommand_shouldReturnCartDisplayResult() {
        // Mobilya (DefaultItem) - VasItem eklenebilir
        cart.addItem(ItemFactory.create(1, 1001, 10, 10000.0, 1));
        cart.addVasItemToItem(1, ItemFactory.createVasItem(2, 3242, 5003, 100.0, 1));
        
        // Dijital Ürün - VasItem eklenemez
        cart.addItem(ItemFactory.create(3, 7889, 20, 500.0, 1));

        ParsedCommand command = new ParsedCommand(CommandType.displayCart, null);

        CommandResult result = applicationService.execute(command);

        assertThat(result.result()).isTrue();
        assertThat(result.message()).isInstanceOf(CartDisplayResult.class);

        CartDisplayResult displayResult = (CartDisplayResult) result.message();

        assertThat(displayResult.items()).hasSize(2);
        
        // Total price = 10000 + 100 + 500 = 10600
        // Promotion = 10600 için 1000 TL indirim.
        // Beklenen fiyat = 9600.0 TL
        assertThat(displayResult.totalPrice()).isEqualTo(9600.0);
        assertThat(displayResult.totalDiscount()).isEqualTo(1000.0);
    }
}
