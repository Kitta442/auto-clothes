package com.grinko.autoclothes.util;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.tuple.Pair;
import org.joda.time.Duration;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static com.grinko.autoclothes.util.LangUtil.ifNonNull;
import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.UUID.nameUUIDFromBytes;
import static java.util.stream.Collectors.joining;
import static org.joda.time.DateTimeUtils.currentTimeMillis;

/**
 * Handy methods related to language types handling.
 */
@Slf4j
public final class AuxUtil {

    private static final Duration SLEEP_TIMEOUT = Duration.millis(100);

    private AuxUtil() {
    }

    /**
     * Creates a pair of key and value
     *
     * @param key   - key
     * @param value - value
     * @param <K>   - key type
     * @param <V>   - value type
     * @return pair of key and value
     */
    public static <K, V> Pair<K, V> pair(final K key, final V value) {
        return Pair.of(key, value);
    }


    /**
     * Await condition. sleep 32 ms
     *
     * @param condition   - condition
     * @param waitTimeout - timeout
     * @throws TimeoutException when timeout elapsed
     */
    public static void await(final Supplier<Boolean> condition,
                             final Duration waitTimeout) throws TimeoutException {

        await(condition, SLEEP_TIMEOUT, waitTimeout);
    }

    /**
     * Await condition. sleep 32 ms
     *
     * @param condition    - condition
     * @param waitTimeout  - timeout
     * @param sleepTimeout - sleepMs
     * @throws TimeoutException when timeout elapsed
     */
    public static void await(final Supplier<Boolean> condition,
                             final Duration sleepTimeout,
                             final Duration waitTimeout) throws TimeoutException {

        val startTime = currentTimeMillis();

        val sleepMs = sleepTimeout.getMillis();
        val waitMs = waitTimeout.getMillis();
        while (!condition.get()) {
            interruptibleSleep(sleepMs);
            if (currentTimeMillis() - startTime > waitMs) {
                throw new TimeoutException("Timeout elapsed.");
            }
        }
    }

    /**
     * Sleep currently executing thread for the specified number of milliseconds
     *
     * @param millis - number of milliseconds
     */
    public static void interruptibleSleep(final long millis) {

        try {
            sleep(millis);
        } catch (InterruptedException e) {
            currentThread().interrupt();
        }
    }

    /**
     * Executes code silently
     *
     * @param callback - callback
     */
    public static void doSilently(final Action0 callback) {

        try {
            callback.run();
        } catch (RuntimeException e) {
            log.error("Error of callback execution", e);
        }
    }

    /**
     * Executes code silently
     *
     * @param runnable - runnable
     * @return silent runnable
     */
    public static Runnable safeRun(final Runnable runnable) {

        return () -> {
            try {
                runnable.run();
            } catch (RuntimeException e) {
                log.error("Error of execution", e);
            }
        };
    }

    public static Runnable safeRun(final Action0 action) {

        return safeRun(
            (Runnable) action::run
        );
    }

    /**
     * Executes code silently
     *
     * @param callback     - callback
     * @param defaultValue - default value
     * @param <T>          - type of result object
     * @return a result object, or {@code null}
     */
    public static <T> T safeCall(final Callback<T> callback, final T defaultValue) {

        T result;
        try {
            result = callback.exec();
        } catch (RuntimeException e) {
            log.error("Error of callback execution", e);
            result = callback.onError(e);
            if (isNull(result)) {
                result = defaultValue;
            }
        }
        return result;
    }

    /**
     * Executes code silently
     *
     * @param callback - callback
     * @param <T>      - type of result object
     * @return a result object, or {@code null}
     */
    public static <T> T safeCall(final Callback<T> callback) {

        return safeCall(callback, null);
    }


    @SneakyThrows
    public static String hash(final Object... arr) {

        if (nonNull(arr)) {
            return nameUUIDFromBytes(
                Stream.of(arr)
                    .filter(Objects::nonNull)
                    .map(Object::toString)
                    .collect(joining("-"))
                    .getBytes(StandardCharsets.UTF_8)
            ).toString();
        }

        return nameUUIDFromBytes("".getBytes())
            .toString();
    }

    public static <T> Supplier<T> lazy(final Supplier<T> loader) {

        return new LazyValue<>(loader);
    }

    public static void doQuietly(final Action0 action) {
        ifNonNull(
            action,
            a -> {
                try {
                    action.run();
                } catch (final Exception e) {
                    log.warn("Error when trying do something quietly: {}", e.getMessage());
                }
            }
        );
    }


}
