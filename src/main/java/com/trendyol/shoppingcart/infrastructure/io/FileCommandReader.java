package com.trendyol.shoppingcart.infrastructure.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * Reads input file line by line.
 */
public class FileCommandReader implements AutoCloseable {

    private final Stream<String> stream;

    public FileCommandReader(Path inputPath) throws IOException {
        // Dosyayı satır satır okuyup bir Stream nesnesine çeviriyoruz
        this.stream = Files.lines(inputPath);
    }

    public Stream<String> lines() {
        return this.stream;
    }

    @Override
    public void close() {
        if (this.stream != null) {
            this.stream.close();
        }
    }
}
