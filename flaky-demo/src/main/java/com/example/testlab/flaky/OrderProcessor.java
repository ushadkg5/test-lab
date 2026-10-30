package com.example.testlab.flaky;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/** Processes orders on a background thread, so results appear some time after submit(). */
public class OrderProcessor implements AutoCloseable {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Set<String> processed = ConcurrentHashMap.newKeySet();

    public void submit(String orderId) {
        executor.submit(() -> processed.add(orderId));
    }

    public boolean isProcessed(String orderId) {
        return processed.contains(orderId);
    }

    @Override
    public void close() {
        executor.shutdownNow();
    }
}
