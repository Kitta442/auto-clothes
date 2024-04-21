package com.grinko.autoclothes.util;

import com.google.common.collect.Iterables;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static java.util.Arrays.stream;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

@Slf4j
public final class CollectionUtil {

    private CollectionUtil() {
    }

    public static <K, V> List<Map<K, V>> splitMap(final Map<K, V> map,
                                                  final int size) {

        final List<Map<K, V>> result = new ArrayList<>();
        final Iterable<List<Map.Entry<K, V>>> chunks = Iterables.partition(map.entrySet(), size);
        chunks
            .forEach(
                entries -> result.add(
                    entries
                        .stream()
                        .collect(
                            toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue
                            )
                        )
                )
            );
        return result;
    }


    public static <F, T> List<T> map2list(final Collection<F> src,
                                          final Predicate<F> filter,
                                          final Function<F, T> fun) {

        if (isNull(src)) {
            return new ArrayList<>();
        }
        return src.stream().filter(filter).map(fun).collect(toList());
    }

    public static <T> List<T> sortedList(final Collection<T> src,
                                         final Comparator<T> comparator) {

        if (isNull(src)) {
            return new ArrayList<>();
        }
        return src.stream().sorted(comparator).collect(toList());
    }

    public static <F, T> List<T> map2list(final Collection<F> src,
                                          final Function<F, T> fun) {

        if (isNull(src)) {
            return new ArrayList<>();
        }
        return src.stream().map(fun).collect(toList());
    }

    public static <T> List<T> map2list(final Collection<T> src) {

        if (isNull(src)) {
            return new ArrayList<>();
        }
        return new ArrayList<>(src);
    }

    public static Map<String, Object> mapOf(final Object... args) {

        val res = new HashMap<String, Object>();

        if (isNull(args)) {
            return res;
        }
        if (args.length % 2 != 0) {
            ExceptionUtil.panic("Not paired arguments");
        }

        for (int i = 0; i < args.length - 1; i += 2) {
            val k = args[i];
            if (isNull(k)) {
                ExceptionUtil.panic("Key is null");
            }
            res.put(k.toString(), args[i + 1]);
        }

        return res;
    }


    public static <T> Set<T> map2set(final Collection<T> src) {

        return LangUtil.nvl(
            src,
            c -> c.stream()
                .filter(Objects::nonNull)
                .collect(toSet()),
            emptySet()
        );
    }

    public static <K, V> V putIfAbsent(final Map<K, V> map,
                                       final K key,
                                       final Supplier<V> value) {

        V v = map.get(key);
        if (isNull(v)) {
            v = value.get();
            map.put(key, v);
        }
        return v;
    }

    /**
     * Filters array with provided predicated
     *
     * @param toFilter  array to filter
     * @param predicate predicate
     * @param <T>       array item type
     * @return list of values satisfying to predicate contition
     */
    public static <T> List<T> filter(final T[] toFilter,
                                     final Predicate<T> predicate) {

        return stream(toFilter).filter(predicate).collect(toList());
    }

    /**
     * Filters list with provided predicated
     *
     * @param toFilter  list to filter
     * @param predicate predicate
     * @param <T>       list item type
     * @return list of values satisfying to predicate contition
     */
    public static <T> List<T> filter(final List<T> toFilter,
                                     final Predicate<T> predicate) {

        return toFilter.stream().filter(predicate).collect(toList());
    }

    /**
     * Maps list with provided mapper
     *
     * @param toMap  list to filter
     * @param mapper map function
     * @param <T>    source list item type
     * @param <R>    result list item type
     * @return list of values satisfying to predicate contition
     */
    public static <T, R> List<R> map(final List<T> toMap,
                                     final Function<T, R> mapper) {

        if (CollectionUtils.isEmpty(toMap)) {
            return emptyList();
        }
        return toMap.stream().map(mapper).collect(toList());
    }

}
