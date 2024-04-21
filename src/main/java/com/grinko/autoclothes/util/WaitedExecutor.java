package com.grinko.autoclothes.util;


import java.util.Collection;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public interface WaitedExecutor {

    Signal DUMMY_SIGNAL = new Signal() {
        @Override
        public void await() {
            //nop
        }

        @Override
        public void awaitAndThen(final Action0 action0) {
            action0.run();
        }

    };

    default Signal execute(final Action0... actions) {
        return execute(
            Stream.of(actions)
                .collect(toList())
        );
    }

    Signal execute(Collection<Action0> actions);

    default void terminate() {
        //nop
    }

    interface Signal {
        void await();

        void awaitAndThen(Action0 action0);

    }
}
