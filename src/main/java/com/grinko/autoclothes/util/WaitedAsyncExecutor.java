package com.grinko.autoclothes.util;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.time.Duration;
import java.util.Collection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.grinko.autoclothes.exception.ExceptionFactory.whenAsyncError;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

@Slf4j
@RequiredArgsConstructor
public class WaitedAsyncExecutor implements WaitedExecutor {

    private final Duration awaitTimeout;
    private final ExecutorService executorService;

    private static Runnable runnable(final MutableHolder<Exception> ifError,
                                     final CountDownLatch latch,
                                     final Action0 action) {
        return () -> {
            try {
                action.run();
            } catch (Exception t) {
                log.error("Async execution error", t);
                ifError.applyIfNull(() -> t);
            } finally {
                latch.countDown();
            }
        };
    }

    @SneakyThrows
    @Override
    public void terminate() {
        executorService.shutdownNow();
    }

    @Override
    public Signal execute(final Collection<Action0> actions) {

        if (isNotEmpty(actions)) {
            val error = new MutableHolder<Exception>();
            val latch = new CountDownLatch(actions.size());

            actions.stream()
                .map(i -> runnable(error, latch, i))
                .forEach(executorService::execute);

            return signal(error, latch);
        } else {
            return DUMMY_SIGNAL;
        }
    }

    private Signal signal(final MutableHolder<Exception> ifError,
                          final CountDownLatch latch) {
        return new Signal() {

            @SneakyThrows
            @Override
            public void await() {

                if (!latch.await(awaitTimeout.toMillis(), TimeUnit.MILLISECONDS)) {
                    throw new TimeoutException("Cant await until async method finished within "
                        + awaitTimeout.toMillis() + "ms");
                }

                if (!ifError.isNull()) {
                    val e = ifError.get();
                    throw whenAsyncError("Thrown error while async running: " + e.getMessage(), e);
                }
            }

            @Override
            public void awaitAndThen(final Action0 action0) {
                await();
                action0.run();
            }
        };
    }
}
