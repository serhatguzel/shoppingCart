package com.trendyol.shoppingcart;

import java.nio.file.Path;

import com.trendyol.shoppingcart.application.CartApplicationService;
import com.trendyol.shoppingcart.domain.model.cart.Cart;
import com.trendyol.shoppingcart.domain.service.PromotionEngine;
import com.trendyol.shoppingcart.infrastructure.io.FileCommandReader;
import com.trendyol.shoppingcart.infrastructure.io.FileResultWriter;
import com.trendyol.shoppingcart.infrastructure.json.JsonCommandParser;
import com.trendyol.shoppingcart.infrastructure.json.ParsedCommand;

/**
 * Entry point of the Trendyol Shopping Cart Application.
 *
 * Reads JSON commands from an input file (one command per line),
 * executes each command, and writes results to an output file.
 *
 * Usage:
 *   java -jar shopping-cart.jar <input-file> <output-file>
 */
public class ShoppingCartApplication {

    public static void main(String[] args) {
        // 1. Dışarıdan gelen argümanları kontrol edelim
        if (args.length < 2) {
            System.err.println("Eksik argüman! Kullanım: java -jar app.jar <input.json> <output.json>");
            System.exit(1);
        }

        Path inputPath = Path.of(args[0]);
        Path outputPath = Path.of(args[1]);

        // 2. Uygulamamızın kalbini (Domain ve Application servislerini) oluşturuyoruz
        Cart cart = new Cart();
        PromotionEngine promotionEngine = new PromotionEngine();
        CartApplicationService applicationService = new CartApplicationService(cart, promotionEngine);
        JsonCommandParser parser = new JsonCommandParser();

        // 3. Dosyaları açıyoruz ve işlemeye başlıyoruz
        try (FileCommandReader reader = new FileCommandReader(inputPath);
             FileResultWriter writer = new FileResultWriter(outputPath)) {

            System.out.println("Okunacak dosya: " + inputPath);
            System.out.println("Yazılacak dosya: " + outputPath);

            // Her bir satırı (komutu) sırayla oku
            reader.lines().forEach(line -> {
                if (line == null || line.trim().isEmpty()) {
                    return; // Boş satırları atla
                }

                try {
                    // String metni -> ParsedCommand nesnesine çevir
                    ParsedCommand command = parser.parse(line);
                    
                    // Komutu çalıştır ve sonucu al
                    var result = applicationService.execute(command);
                    
                    // Sonucu çıktı dosyasına yaz
                    writer.write(result);
                    
                } catch (Exception e) {
                    System.err.println("Satır işlenirken hata oluştu (" + line + "): " + e.getMessage());
                }
            });

            System.out.println("Tüm komutlar başarıyla işlendi!");

        } catch (Exception e) {
            System.err.println("Dosya operasyonlarında kritik bir hata oluştu: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
