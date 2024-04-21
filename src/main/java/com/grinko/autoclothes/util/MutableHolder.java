package com.grinko.autoclothes.util;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import static java.util.Objects.nonNull;

/**
 * MutableHolder
 *
 * @param <T> - holded value type
 */
@Accessors(chain = true)
@Getter
@Setter
@ToString(doNotUseGetters = true, includeFieldNames = false)
public class MutableHolder<T> implements Supplier<T>, Consumer<T> {

    private T nested;

    /**
     * Default constructor
     */
    public MutableHolder() {
        this(null);
    }

    /**
     * Constructor
     *
     * @param nested - nested value
     */
    public MutableHolder(final T nested) {
        this.nested = nested;
    }

    /**
     * Modify
     *
     * @param operator - operator
     */
    public void modify(final UnaryOperator<T> operator) {

        nested = operator.apply(nested);
    }

    /**
     * Modify
     *
     * @param supplier - supplier
     * @return self instance
     */
    public MutableHolder<T> applyIfNull(final Supplier<T> supplier) {

        if (isNull()) {
            nested = supplier.get();
        }
        return this;
    }

    /**
     * Modify
     *
     * @param supplier - supplier
     * @return self instance
     */
    public T ifNull(final Supplier<T> supplier) {

        if (isNull()) {
            nested = supplier.get();
        }
        return nested;
    }

    /**
     * action if null
     *
     * @param action - action
     */
    public void whenNull(final Action0 action) {

        if (isNull()) {
            action.run();
        }
    }

    @Override
    public T get() {
        return nested;
    }

    @Override
    public void accept(final T t) {
        this.nested = t;
    }

    /**
     * Consume if non null
     *
     * @param consumer - consumer
     */
    public void ifNonNull(final Consumer<T> consumer) {
        if (nonNull(this.nested)) {
            consumer.accept(this.nested);
        }
    }

    /**
     * Consume if non null
     *
     * @param consumer - consumer
     */
    public void whenNonNull(final Consumer<T> consumer) {
        if (nonNull(this.nested)) {
            consumer.accept(this.nested);
        }
    }

    /**
     * Throw exception if is null
     *
     * @param exception - exception supplier
     * @param <E>       - type of exception
     * @return value if not null
     * @throws E if value is null
     */
    public <E extends Throwable> T raiseIfNul(final Supplier<? extends E> exception) throws E {

        if (isNull()) {
            throw exception.get();
        }
        return this.nested;
    }

    /**
     * map
     *
     * @param fun - fun
     * @param <D> - dest type
     * @return list
     */
    public <D> Optional<D> map(final Function<T, D> fun) {

        return optional().map(fun);
    }

    public Optional<T> optional() {

        return Optional.ofNullable(this.nested);
    }


    /**
     * isNull
     *
     * @return isNull
     */
    public boolean isNull() {

        return Objects.isNull(this.nested);
    }
}