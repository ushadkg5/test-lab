package com.example.testlab.flaky;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * FLAKY: expects a fixed order from a Set. Set.of() iteration order changes between
 * JVM runs, so this passes on some runs and fails on others. Fixed in {@link TagServiceTest}.
 */
@Tag("flaky")
class TagServiceFlakyTest {

    @Test
    void returnsDefaultTags() {
        assertThat(new TagService().defaultTags())
                .containsExactly("java", "maven");
    }
}
