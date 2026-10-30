package com.example.testlab.flaky;

import java.time.Clock;
import java.time.LocalTime;

/** Greets based on the time of day. Takes a Clock so tests can control "now". */
public class Greeter {

    private final Clock clock;

    public Greeter() {
        this(Clock.systemDefaultZone());
    }

    public Greeter(Clock clock) {
        this.clock = clock;
    }

    public String greeting() {
        int hour = LocalTime.now(clock).getHour();
        if (hour < 12) {
            return "Good morning";
        }
        return hour < 18 ? "Good afternoon" : "Good evening";
    }
}
