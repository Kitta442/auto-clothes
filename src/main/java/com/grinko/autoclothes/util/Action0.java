package com.grinko.autoclothes.util;

import java.util.Objects;

/**
 * Action0
 */
public interface Action0 {

    static Action0 nop() {
        return () -> {
            //nop
        };
    }

    /**
     * Process.
     */
    void run();

    default Action0 andThen(final Action0 after) {

        Objects.requireNonNull(after);
        return () -> {
            run();
            after.run();
        };
    }
}