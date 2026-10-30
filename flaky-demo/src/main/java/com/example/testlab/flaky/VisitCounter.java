package com.example.testlab.flaky;

public class VisitCounter {

    private int count;

    public void record() {
        count++;
    }

    public int count() {
        return count;
    }
}
