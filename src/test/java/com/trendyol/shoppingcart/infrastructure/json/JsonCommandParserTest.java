package com.trendyol.shoppingcart.infrastructure.json;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.trendyol.shoppingcart.application.dto.command.AddItemCommand;
import com.trendyol.shoppingcart.application.dto.command.AddVasItemCommand;
import com.trendyol.shoppingcart.application.dto.command.RemoveItemCommand;

class JsonCommandParserTest {

    private JsonCommandParser parser;

    @BeforeEach
    void setUp() {
        parser = new JsonCommandParser();
    }

    @Test
    @DisplayName("Should parse addItem JSON correctly")
    void parse_whenAddItemJson_shouldReturnParsedCommandWithAddItemCommandPayload() {
        String json = "{\"command\":\"addItem\",\"payload\":{\"itemId\":1,\"categoryId\":1001,\"sellerId\":1,\"price\":100.0,\"quantity\":2}}";
        
        ParsedCommand result = parser.parse(json);

        assertThat(result.type()).isEqualTo(CommandType.addItem);
        assertThat(result.payload()).isInstanceOf(AddItemCommand.class);
        
        AddItemCommand payload = (AddItemCommand) result.payload();
        assertThat(payload.itemId()).isEqualTo(1);
        assertThat(payload.categoryId()).isEqualTo(1001);
        assertThat(payload.sellerId()).isEqualTo(1);
        assertThat(payload.price()).isEqualTo(100.0);
        assertThat(payload.quantity()).isEqualTo(2);
    }

    @Test
    @DisplayName("Should parse addVasItemToItem JSON correctly")
    void parse_whenAddVasItemJson_shouldReturnParsedCommandWithAddVasItemCommandPayload() {
        String json = "{\"command\":\"addVasItemToItem\",\"payload\":{\"itemId\":1,\"vasItemId\":100,\"categoryId\":3242,\"sellerId\":5003,\"price\":50.0,\"quantity\":1}}";
        
        ParsedCommand result = parser.parse(json);

        assertThat(result.type()).isEqualTo(CommandType.addVasItemToItem);
        assertThat(result.payload()).isInstanceOf(AddVasItemCommand.class);
        
        AddVasItemCommand payload = (AddVasItemCommand) result.payload();
        assertThat(payload.itemId()).isEqualTo(1);
        assertThat(payload.vasItemId()).isEqualTo(100);
        assertThat(payload.price()).isEqualTo(50.0);
    }

    @Test
    @DisplayName("Should parse removeItem JSON correctly")
    void parse_whenRemoveItemJson_shouldReturnParsedCommandWithRemoveItemCommandPayload() {
        String json = "{\"command\":\"removeItem\",\"payload\":{\"itemId\":2}}";
        
        ParsedCommand result = parser.parse(json);

        assertThat(result.type()).isEqualTo(CommandType.removeItem);
        assertThat(result.payload()).isInstanceOf(RemoveItemCommand.class);
        
        RemoveItemCommand payload = (RemoveItemCommand) result.payload();
        assertThat(payload.itemId()).isEqualTo(2);
    }

    @Test
    @DisplayName("Should parse displayCart JSON with null payload")
    void parse_whenDisplayCartJson_shouldReturnParsedCommandWithNullPayload() {
        String json = "{\"command\":\"displayCart\"}";
        
        ParsedCommand result = parser.parse(json);

        assertThat(result.type()).isEqualTo(CommandType.displayCart);
        assertThat(result.payload()).isNull();
    }

    @Test
    @DisplayName("Should throw RuntimeException when JSON is invalid")
    void parse_whenInvalidJson_shouldThrowRuntimeException() {
        String invalidJson = "{\"command\":\"invalidCommand\""; // Kasıtlı olarak hatalı JSON (parantez eksik)
        
        assertThatThrownBy(() -> parser.parse(invalidJson))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("JSON ayrıştırılamadı");
    }
}
