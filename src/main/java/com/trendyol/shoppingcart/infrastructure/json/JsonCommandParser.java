package com.trendyol.shoppingcart.infrastructure.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trendyol.shoppingcart.application.dto.command.AddItemCommand;
import com.trendyol.shoppingcart.application.dto.command.AddVasItemCommand;
import com.trendyol.shoppingcart.application.dto.command.RemoveItemCommand;

public class JsonCommandParser {

    private final ObjectMapper objectMapper;

    public JsonCommandParser() {
        this.objectMapper = new ObjectMapper();
    }

    public ParsedCommand parse(String json) {
        try {
            // 1. JSON metnini okuyup ağaç yapısına (Node) dönüştürüyoruz
            JsonNode rootNode = objectMapper.readTree(json);

            // 2. "command" alanını okuyup Enum'a çeviriyoruz
            String commandString = rootNode.get("command").asText();
            CommandType commandType = CommandType.valueOf(commandString);

            // 3. "payload" alanını alıyoruz
            JsonNode payloadNode = rootNode.get("payload");
            Object payload = null;

            // 4. Eğer payload doluysa, komutun tipine uygun Record'a çeviriyoruz
            if (payloadNode != null && !payloadNode.isNull()) {
                switch (commandType) {
                    case addItem:
                        payload = objectMapper.treeToValue(payloadNode, AddItemCommand.class);
                        break;
                    case addVasItemToItem:
                        payload = objectMapper.treeToValue(payloadNode, AddVasItemCommand.class);
                        break;
                    case removeItem:
                        payload = objectMapper.treeToValue(payloadNode, RemoveItemCommand.class);
                        break;
                    default:
                        // resetCart ve displayCart komutlarının payload'u yoktur, null kalır.
                        break;
                }
            }

            // Her şeyi ParsedCommand içine paketleyip yolluyoruz!
            return new ParsedCommand(commandType, payload);
        } catch (Exception e) {
            throw new RuntimeException("JSON ayrıştırılamadı (Parsing Error): " + json, e);
        }
    }
}
