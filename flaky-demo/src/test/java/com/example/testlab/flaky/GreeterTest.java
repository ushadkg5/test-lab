package com.example.testlab.flaky;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;

/** FIXED: each test pins the time with a fixed Clock. */
class GreeterTest {

    private static Greeter greeterAt(String isoInstant) {
        return new Greeter(Clock.fixed(Instant.parse(isoInstant), ZoneOffset.UTC));
    }

    @Test
    void greetsWithGoodMorningBeforeNoon() {
        assertThat(greeterAt("2026-01-15T09:00:00Z").greeting()).isEqualTo("Good morning");
    }

    @Test
    void greetsWithGoodAfternoonFromNoon() {
        assertThat(greeterAt("2026-01-15T12:00:00Z").greeting()).isEqualTo("Good afternoon");
    }

    @Test
    void greetsWithGoodEveningFromSixPm() {
        assertThat(greeterAt("2026-01-15T18:00:00Z").greeting()).isEqualTo("Good evening");
    }
}
