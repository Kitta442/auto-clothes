package com.grinko.autoclothes.job;

import com.grinko.autoclothes.util.Action0;
import lombok.RequiredArgsConstructor;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class SomeInitialJob implements Action0 {

    private final Consumer<Integer> consumer;

    @Override
    public void run() {
        //some calculations
        consumer.accept(15);
    }
}
