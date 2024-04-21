package com.grinko.autoclothes.util;

import java.util.function.Consumer;

public interface Acceptor<T> {

    void accept(Consumer<T> consumer);

}
