package com.grinko.autoclothes.util;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Slf4j
public final class LangUtil {

    private static final BigDecimal BYTES_IN_MB = new BigDecimal(1_048_576);

    private LangUtil() {
    }

    /**
     * Null operator
     *
     * @param <T> - type
     * @return Null operator
     */
    public static <T> Consumer<T> nop() {
        return t -> {
        };
    }

    /**
     * Null operator
     *
     * @param <X> - type
     * @param <Y> - type
     * @return Null operator
     */
    public static <X, Y> BiConsumer<X, Y> binop() {
        return (x, y) -> {
        };
    }

    /**
     * withAs
     *
     * @param f        from
     * @param type     type
     * @param consumer consumer
     * @param <F>      from type
     * @param <T>      tu type
     */
    public static <F, T extends F> void withAs(final F f,
                                               final Class<T> type,
                                               final Consumer<T> consumer) {

        consumer.accept(type.cast(f));
    }

    /**
     * withAs
     *
     * @param f        from
     * @param type     type
     * @param consumer consumer
     * @param <F>      from type
     * @param <T>      tu type
     */
    public static <F, T extends F> void withAsIf(final F f,
                                                 final Class<T> type,
                                                 final Consumer<T> consumer) {

        if (type.isInstance(f)) {
            consumer.accept(type.cast(f));
        }
    }

    public static <F, T extends F> void withAsIf(final F f,
                                                 final Class<T> type,
                                                 final Consumer<T> consumer,
                                                 final Consumer<F> ifNot) {

        if (type.isInstance(f)) {
            consumer.accept(type.cast(f));
        } else {
            ifNot.accept(f);
        }
    }

    public static Long mega2bytes(final Double d) {

        return nvl(
            d,
            i -> BigDecimal.valueOf(i)
                .multiply(BYTES_IN_MB).longValue()
        );
    }

    public static Long mega2bytes(final BigDecimal d) {

        return nvl(d, i -> mega2bytes(d.doubleValue()));
    }

    public static BigDecimal bytes2mega(final Long d) {

        return nvl(
            d,
            i -> BigDecimal.valueOf(i)
                .divide(BYTES_IN_MB, 1, RoundingMode.HALF_UP)
        );
    }


    /**
     * Replace value if is null with default value
     *
     * @param val      - value
     * @param defValue - default value
     * @param <T>      the type of instance that can be contained.
     * @return default value if original value is null
     */
    public static <T> T nvl(final T val,
                            final T defValue) {

        if (isNull(val)) {
            return defValue;
        }
        return val;
    }

    /**
     * Replace value if is null with default value
     *
     * @param val      - value
     * @param fun      - function to get not null value
     * @param defValue - default value
     * @param <T>      the type of instance to return.
     * @param <V>      the type of origin value.
     * @return default value if original value is null
     */
    public static <V, T> T nvl(final V val,
                               final Function<V, T> fun,
                               final T defValue) {

        if (isNull(val)) {
            return defValue;
        }
        return fun.apply(val);
    }

    /**
     * Replace value if is null with default value
     *
     * @param val - value
     * @param fun - function to get not null value
     * @param <T> the type of instance to return.
     * @param <V> the type of origin value.
     * @return default value if original value is null
     */
    public static <V, T> T nvl(final V val,
                               final Function<V, T> fun) {

        if (isNull(val)) {
            return null;
        }
        return fun.apply(val);
    }


    /**
     * Checks a value val and pass it to a consumer if it is not null.
     *
     * @param val      the value to be checked.
     * @param consumer the {@link Consumer} the value pass to.
     * @param <T>      the type of instance to check.
     */
    public static <T> void ifNonNull(final T val,
                                     final Consumer<T> consumer) {
        if (nonNull(val)) {
            consumer.accept(val);
        }
    }

    public static <T> void ifNonNullOr(final T val,
                                       final Consumer<T> consumer,
                                       final Action0 whenNull) {

        if (nonNull(val)) {
            consumer.accept(val);
        } else {
            whenNull.run();
        }
    }

    /**
     * ifNonBlank
     *
     * @param str      - str
     * @param consumer - consumer
     */
    public static void ifNonBlank(final String str,
                                  final Consumer<String> consumer) {

        if (isNotBlank(str)) {
            consumer.accept(str);
        }
    }

    public static void ifNonBlank(final String str,
                                  final Consumer<String> consumer,
                                  final Action0 whenBlank) {

        if (isNotBlank(str)) {
            consumer.accept(str);
        } else {
            whenBlank.run();
        }
    }

    /**
     * ifNonEmpty
     *
     * @param val      - val
     * @param consumer - consumer
     * @param <T>      - type
     */
    public static <T> void ifNonEmpty(final Collection<T> val,
                                      final Consumer<Collection<T>> consumer) {

        if (isNotEmpty(val)) {
            consumer.accept(val);
        }
    }

    /**
     * Determines whether two possibly-null objects are equal. Returns:
     *
     * <ul>
     * <li>{@code true} if {@code a} and {@code b} are both null.
     * <li>{@code true} if {@code a} and {@code b} are both non-null and they are
     *     equal according to {@link Object#equals(Object)}.
     * <li>{@code false} in all other situations.
     * </ul>
     *
     * @param a - left object to compare.
     * @param b - right object to compare.
     * @return true if {@code a} and {@code b} are equal
     */
    public static boolean eq(final Object a,
                             final Object b) {

        return Objects.equals(a, b);
    }

    /**
     * Replace string value if is blank with default value
     *
     * @param val      - value
     * @param fun      - function to get not null value
     * @param defValue - default value
     * @param <T>      the type of instance to return.
     * @return default value if original value is null
     */
    public static <T> T bvl(final String val,
                            final Function<String, T> fun,
                            final T defValue) {

        if (isBlank(val)) {
            return defValue;
        }
        return fun.apply(val);
    }


    /**
     * Replace value if is null with default value
     *
     * @param val - value
     * @param fun - function to get not null value
     * @param <T> the type of instance to return.
     * @return default value if original value is null
     */
    public static <T> T bvl(final String val,
                            final Function<String, T> fun) {

        if (isBlank(val)) {
            return null;
        }
        return fun.apply(val);
    }

    public static String bvl(final String val,
                             final String defValue) {

        if (isBlank(val)) {
            return defValue;
        }
        return val;
    }
}
