package com.grinko.autoclothes.exception;

public class IllegalAsyncStateException extends IllegalStateException {

    public IllegalAsyncStateException(final String errorCode,
                                      final Throwable cause) {

        super(errorCode, cause);
    }
}
