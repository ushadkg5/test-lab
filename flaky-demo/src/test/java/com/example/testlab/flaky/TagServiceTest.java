package com.example.testlab.flaky;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/** FIXED: checks the contents without assuming an order. */
class TagServiceTest {

    @Test
    void returnsDefaultTags() {
        assertThat(new TagService().defaultTags())
                .containsExactlyInAnyOrder("java", "maven");
    }
}
