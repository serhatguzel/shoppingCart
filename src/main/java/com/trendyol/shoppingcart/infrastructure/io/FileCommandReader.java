package com.trendyol.shoppingcart.infrastructure.io;

import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * Reads input file line by line.
 * TODO: Phase 6
 */
public class FileCommandReader implements AutoCloseable {

    public FileCommandReader(Path inputPath) {
        // TODO: Phase 6
    }

    public Stream<String> lines() {
        // TODO: Phase 6
        throw new UnsupportedOperationException("TODO: Phase 6");
    }

    @Override
    public void close() throws Exception {
        // TODO: Phase 6
    }
}
