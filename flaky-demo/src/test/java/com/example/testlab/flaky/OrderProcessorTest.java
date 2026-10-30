package com.example.testlab.flaky;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import java.time.Duration;
import org.junit.jupiter.api.Test;

/** FIXED: waits explicitly for the condition instead of assuming the work is already done. */
class OrderProcessorTest {

    @Test
    void marksOrderAsProcessedAfterSubmit() {
        try (OrderProcessor processor = new OrderProcessor()) {
            processor.submit("order-1");

            await().atMost(Duration.ofSeconds(2))
                    .untilAsserted(() -> assertThat(processor.isProcessed("order-1")).isTrue());
        }
    }
}
