package com.trendyol.shoppingcart.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.trendyol.shoppingcart.ShoppingCartApplication;

class ShoppingCartIntegrationTest {

    private Path inputPath;
    private Path outputPath;

    @BeforeEach
    void setUp() throws Exception {
        inputPath = Files.createTempFile("input", ".json");
        outputPath = Files.createTempFile("output", ".json");
    }

    @AfterEach
    void tearDown() throws Exception {
        Files.deleteIfExists(inputPath);
        Files.deleteIfExists(outputPath);
    }

    @Test
    @DisplayName("Should process entire JSON file end-to-end correctly")
    void testEndToEndScenario() throws Exception {
        // 1. Örnek JSON komutlarını input dosyasına yaz
        String jsonCommands = """
                {"command":"addItem","payload":{"itemId":1,"categoryId":1001,"sellerId":1,"price":100.0,"quantity":2}}
                {"command":"addItem","payload":{"itemId":2,"categoryId":3004,"sellerId":1,"price":200.0,"quantity":1}}
                {"command":"addVasItemToItem","payload":{"itemId":1,"vasItemId":100,"categoryId":3242,"sellerId":5003,"price":50.0,"quantity":1}}
                {"command":"displayCart"}
                {"command":"removeItem","payload":{"itemId":2}}
                {"command":"displayCart"}
                {"command":"resetCart"}
                """;

        Files.writeString(inputPath, jsonCommands);

        // 2. Uygulamayı (Main metodunu) argümanlarla çalıştır
        String[] args = { inputPath.toAbsolutePath().toString(), outputPath.toAbsolutePath().toString() };
        ShoppingCartApplication.main(args);

        // 3. Oluşan Output dosyasını oku
        List<String> outputLines = Files.readAllLines(outputPath);

        // 4. Beklenen sonuçları doğrula (Tıpkı Phase 6 planımızdaki gibi)
        assertThat(outputLines).hasSize(7);

        // 1. addItem (1001)
        assertThat(outputLines.get(0)).contains("\"result\":true").contains("Item added successfully");

        // 2. addItem (3004)
        assertThat(outputLines.get(1)).contains("\"result\":true").contains("Item added successfully");

        // 3. addVasItem
        assertThat(outputLines.get(2)).contains("\"result\":true").contains("VasItem added successfully");

        // 4. displayCart 
        // Brüt sepet tutarı: (100*2) + (200) + (50) = 450 TL
        // SameSellerPromotion (9909): 45 TL indirim
        // TotalPricePromotion (1232): 250 TL indirim (Çünkü 5000 TL'den az)
        // Beklenen indirim: 250 TL. Final Fiyat: 450 - 250 = 200 TL
        assertThat(outputLines.get(3)).contains("\"totalPrice\":200.0").contains("\"appliedPromotionId\":1232").contains("\"totalDiscount\":250.0");

        // 5. removeItem (İkinci ürün silinir)
        assertThat(outputLines.get(4)).contains("\"result\":true").contains("Item removed successfully");

        // 6. displayCart 
        // Brüt sepet tutarı: (100*2) + (50) = 250 TL
        // TotalPricePromotion (1232): 250 TL indirim! (İndirim sepet tutarı kadar olabilir)
        // Final Fiyat: 250 - 250 = 0 TL (Bedava!)
        assertThat(outputLines.get(5)).contains("\"totalPrice\":0.0").contains("\"totalDiscount\":250.0");

        // 7. resetCart
        assertThat(outputLines.get(6)).contains("\"result\":true").contains("Cart reset successfully");
    }
}
