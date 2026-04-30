package com.trendyol.shoppingcart.infrastructure.json;

/** All supported command type names — must match JSON "command" field exactly. */
public enum CommandType {
    addItem,
    addVasItemToItem,
    removeItem,
    resetCart,
    displayCart
}
