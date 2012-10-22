package com.iweb.serverloadbalancer;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class CurrentLoadPercentageMatcher extends TypeSafeMatcher<Server> {
    public static final double EPSILON = 0.01d;
    private final double expectedCurrentLoadPercentage;

    public CurrentLoadPercentageMatcher(final double expectedCurrentLoadPercentage) {
        this.expectedCurrentLoadPercentage = expectedCurrentLoadPercentage;
    }

    @Override
    protected boolean matchesSafely(final Server server) {
        return doublesAreEquals(server.currentLoadPercentage, expectedCurrentLoadPercentage);
    }

    private boolean doublesAreEquals(final double a, final double b) {
        return a == b || Math.abs(a - b) < EPSILON;
    }

    @Override
    public void describeTo(final Description description) {
        description.appendText("a server with current load percentage of ").appendValue(expectedCurrentLoadPercentage);
    }

    @Override
    protected void describeMismatchSafely(final Server item, final Description mismatchDescription) {
        mismatchDescription.appendText("a server with current load percentage of ").appendValue(item.currentLoadPercentage);
    }

    public static Matcher<Server> hasCurrentLoadPercentageOf(final double currentLoadPercentage) {
        return new CurrentLoadPercentageMatcher(currentLoadPercentage);
    }
}
