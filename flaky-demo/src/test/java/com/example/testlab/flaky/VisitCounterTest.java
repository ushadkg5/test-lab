package com.example.testlab.flaky;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/** FIXED: each test gets a fresh counter, so the tests pass in any order. */
@TestMethodOrder(MethodOrderer.Random.class)
class VisitCounterTest {

    private VisitCounter counter;

    @BeforeEach
    void createCounter() {
        counter = new VisitCounter();
    }

    @Test
    void countsOneVisit() {
        counter.record();

        assertThat(counter.count()).isEqualTo(1);
    }

    @Test
    void countsTwoVisits() {
        counter.record();
        counter.record();

        assertThat(counter.count()).isEqualTo(2);
    }
}
