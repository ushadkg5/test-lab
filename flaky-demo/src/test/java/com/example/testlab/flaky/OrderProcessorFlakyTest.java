package com.example.testlab.flaky;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * FLAKY: checks the result straight after submit(), racing the background thread.
 * Passes only when the executor happens to finish first. Fixed in {@link OrderProcessorTest}.
 */
@Tag("flaky")
class OrderProcessorFlakyTest {

    @Test
    void marksOrderAsProcessedAfterSubmit() {
        try (OrderProcessor processor = new OrderProcessor()) {
            processor.submit("order-1");

            assertThat(processor.isProcessed("order-1")).isTrue();
        }
    }
}
