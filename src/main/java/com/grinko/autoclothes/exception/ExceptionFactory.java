package com.grinko.autoclothes.exception;

public final class ExceptionFactory {

    private ExceptionFactory() {
    }

    public static RuntimeException whenAsyncError(final String errorCode,
                                                  final Throwable cause) {

        return new IllegalAsyncStateException(errorCode, cause);
    }
}
