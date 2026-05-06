package com.trendyol.shoppingcart.application;

import com.trendyol.shoppingcart.application.dto.command.AddItemCommand;
import com.trendyol.shoppingcart.application.dto.command.AddVasItemCommand;
import com.trendyol.shoppingcart.application.dto.command.RemoveItemCommand;
import com.trendyol.shoppingcart.application.dto.response.CartDisplayResult;
import com.trendyol.shoppingcart.application.dto.response.CommandResult;
import com.trendyol.shoppingcart.domain.exception.CartException;
import com.trendyol.shoppingcart.domain.model.cart.Cart;
import com.trendyol.shoppingcart.domain.model.item.Item;
import com.trendyol.shoppingcart.domain.model.item.ItemFactory;
import com.trendyol.shoppingcart.domain.model.item.VasItem;
import com.trendyol.shoppingcart.domain.service.PromotionEngine;
import com.trendyol.shoppingcart.domain.service.PromotionEngine.AppliedPromotion;
import com.trendyol.shoppingcart.infrastructure.json.ParsedCommand;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Application Service — orchestrates use cases.
 * Delegates to domain, catches exceptions, returns CommandResult.
 */
public class CartApplicationService {

    private final Cart cart;
    private final PromotionEngine promotionEngine;

    public CartApplicationService(Cart cart, PromotionEngine promotionEngine) {
        this.cart = cart;
        this.promotionEngine = promotionEngine;
    }

    public CommandResult execute(ParsedCommand command) {
        try {
            switch (command.type()) {
                case addItem:
                    return handleAddItem((AddItemCommand) command.payload());
                case addVasItemToItem:
                    return handleAddVasItem((AddVasItemCommand) command.payload());
                case removeItem:
                    return handleRemoveItem((RemoveItemCommand) command.payload());
                case resetCart:
                    return handleResetCart();
                case displayCart:
                    return handleDisplayCart();
                default:
                    return CommandResult.failure("Unknown command type: " + command.type());
            }
        } catch (CartException e) {
            // Domain kuralları ihlal edildiğinde fırlatılan tüm hataları burada
            // yakalıyoruz.
            return CommandResult.failure(e.getMessage());
        } catch (Exception e) {
            return CommandResult.failure("Unexpected error: " + e.getMessage());
        }
    }

    private CommandResult handleAddItem(AddItemCommand cmd) {
        Item item = ItemFactory.create(cmd.itemId(), cmd.categoryId(), cmd.sellerId(), cmd.price(), cmd.quantity());
        cart.addItem(item);
        return CommandResult.success("Item added successfully");
    }

    private CommandResult handleAddVasItem(AddVasItemCommand cmd) {
        VasItem vasItem = ItemFactory.createVasItem(cmd.vasItemId(), cmd.categoryId(), cmd.sellerId(), cmd.price(),
                cmd.quantity());
        cart.addVasItemToItem(cmd.itemId(), vasItem);
        return CommandResult.success("VasItem added successfully");
    }

    private CommandResult handleRemoveItem(RemoveItemCommand cmd) {
        cart.removeItem(cmd.itemId());
        return CommandResult.success("Item removed successfully");
    }

    private CommandResult handleResetCart() {
        cart.reset();
        return CommandResult.success("Cart reset successfully");
    }

    private CommandResult handleDisplayCart() {
        Optional<AppliedPromotion> bestPromotion = promotionEngine.findBestPromotion(cart);

        double totalDiscount = bestPromotion.map(AppliedPromotion::discount).orElse(0.0);
        
        // Mantıksal hata kontrolü: İndirim miktarı sepet tutarından büyük olamaz!
        // Eğer sepet 100 TL ise, 250 TL'lik promosyon sepeti bedava yapar (100 TL indirim).
        // Sepetin fiyatı asla eksiye düşmemelidir.
        totalDiscount = Math.min(totalDiscount, cart.getTotalPrice());
        
        int appliedPromotionId = bestPromotion.map(AppliedPromotion::promotionId).orElse(0);
        double finalPrice = cart.getTotalPrice() - totalDiscount;

        List<CartDisplayResult.ItemDisplay> itemDisplays = cart.getItems().stream().map(item -> {
            
            List<CartDisplayResult.VasItemDisplay> vasDisplays;
            if (item instanceof com.trendyol.shoppingcart.domain.model.item.DefaultItem) {
                vasDisplays = ((com.trendyol.shoppingcart.domain.model.item.DefaultItem) item).getVasItems().stream()
                        .map(vas -> new CartDisplayResult.VasItemDisplay(
                                vas.getVasItemId(), vas.getCategoryId(), vas.getSellerId(), vas.getPrice(), vas.getQuantity()))
                        .collect(Collectors.toList());
            } else {
                vasDisplays = java.util.Collections.emptyList();
            }

            return new CartDisplayResult.ItemDisplay(
                    item.getItemId(), item.getCategoryId(), item.getSellerId(), item.getPrice(), item.getQuantity(),
                    vasDisplays);
        }).collect(Collectors.toList());

        CartDisplayResult displayResult = new CartDisplayResult(
                itemDisplays,
                finalPrice,
                appliedPromotionId,
                totalDiscount);

        return CommandResult.success(displayResult);
    }
}
