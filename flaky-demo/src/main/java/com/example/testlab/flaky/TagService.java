package com.example.testlab.flaky;

import java.util.Set;

public class TagService {

    /** Returns the tags as a Set, so callers get no guarantee about iteration order. */
    public Set<String> defaultTags() {
        return Set.of("java", "maven");
    }
}
