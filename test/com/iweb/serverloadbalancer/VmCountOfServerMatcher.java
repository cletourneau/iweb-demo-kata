package com.iweb.serverloadbalancer;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class VmCountOfServerMatcher extends TypeSafeMatcher<Server> {
    private final int expectedVmCount;

    public VmCountOfServerMatcher(final int expectedVmCount) {
        this.expectedVmCount = expectedVmCount;
    }

    @Override
    protected boolean matchesSafely(final Server server) {
        return server.vmCount() == expectedVmCount;
    }

    @Override
    public void describeTo(final Description description) {
        description.appendText("a server with a VM count of ").appendValue(expectedVmCount);
    }

    @Override
    protected void describeMismatchSafely(final Server item, final Description mismatchDescription) {
        mismatchDescription.appendText("a server with a VM count of ").appendValue(item.vmCount());
    }

    public static Matcher<? super Server> hasAVmCountOf(final int vmCount) {
        return new VmCountOfServerMatcher(vmCount);
    }

}
