package com.grinko.autoclothes.util;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class LazyValue<T> implements Supplier<T>, Consumer<T> {

    private final Supplier<T> loader;
    private T value;
    private boolean loaded;

    /**
     * Constructor
     *
     * @param loader - loader
     */
    public LazyValue(final Supplier<T> loader) {

        this.loader = loader;
    }

    @Override
    public T get() {
        if (!loaded) {
            value = loader.get();
            loaded = true;
        }
        return value;
    }

    @Override
    public void accept(final T t) {
        loaded = true;
        value = t;
    }

    public void reset() {
        loaded = false;
    }
}