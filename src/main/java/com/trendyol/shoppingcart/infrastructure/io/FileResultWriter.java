package com.trendyol.shoppingcart.infrastructure.io;

import com.trendyol.shoppingcart.application.dto.response.CommandResult;

import java.nio.file.Path;

/**
 * Writes each CommandResult as a JSON line to the output file.
 * TODO: Phase 6
 */
public class FileResultWriter implements AutoCloseable {

    public FileResultWriter(Path outputPath) {
        // TODO: Phase 6
    }

    public void write(CommandResult result) {
        // TODO: Phase 6
        throw new UnsupportedOperationException("TODO: Phase 6");
    }

    @Override
    public void close() throws Exception {
        // TODO: Phase 6
    }
}
