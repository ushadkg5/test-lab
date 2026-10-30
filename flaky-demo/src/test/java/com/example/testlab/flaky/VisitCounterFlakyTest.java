package com.example.testlab.flaky;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * FLAKY: both tests share one static counter, so each result depends on which test ran first.
 * Random method order makes the dependency visible. Fixed in {@link VisitCounterTest}.
 */
@Tag("flaky")
@TestMethodOrder(MethodOrderer.Random.class)
class VisitCounterFlakyTest {

    private static final VisitCounter COUNTER = new VisitCounter();

    @Test
    void countsOneVisit() {
        COUNTER.record();

        assertThat(COUNTER.count()).isEqualTo(1);
    }

    @Test
    void countsTwoVisits() {
        COUNTER.record();

        assertThat(COUNTER.count()).isEqualTo(2);
    }
}
