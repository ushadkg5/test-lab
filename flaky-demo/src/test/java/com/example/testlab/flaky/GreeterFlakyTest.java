package com.example.testlab.flaky;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * FLAKY: uses the real system clock, so the result depends on when the test runs.
 * Passes before noon, fails after. Fixed in {@link GreeterTest}.
 */
@Tag("flaky")
class GreeterFlakyTest {

    @Test
    void greetsWithGoodMorning() {
        assertThat(new Greeter().greeting()).isEqualTo("Good morning");
    }
}
