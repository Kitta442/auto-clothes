package com.grinko.autoclothes.util;

public interface Callback<T> {

    /**
     * Allows for returning a result object created within the call.
     *
     * @return a result object, or {@code null}
     * @throws RuntimeException on RuntimeException
     */
    T exec() throws RuntimeException;

    /**
     * On error handler
     *
     * @param e - exception
     * @return a result object, or {@code null}
     */
    default T onError(final RuntimeException e) {
        return null;
    }
}