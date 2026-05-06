package com.trendyol.shoppingcart.infrastructure.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trendyol.shoppingcart.application.dto.response.CommandResult;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Writes each CommandResult as a JSON line to the output file.
 */
public class FileResultWriter implements AutoCloseable {

    private final BufferedWriter writer;
    private final ObjectMapper objectMapper;

    public FileResultWriter(Path outputPath) throws IOException {
        // Dosyayı yazmak için bir BufferedWriter açıyoruz
        this.writer = Files.newBufferedWriter(outputPath);
        this.objectMapper = new ObjectMapper();
    }

    public void write(CommandResult result) throws IOException {
        // DTO'yu JSON metnine çeviriyoruz
        String json = objectMapper.writeValueAsString(result);

        // Dosyaya yazıp yeni satıra geçiyoruz
        writer.write(json);
        writer.newLine();
    }

    @Override
    public void close() throws IOException {
        if (this.writer != null) {
            this.writer.close();
        }
    }
}
