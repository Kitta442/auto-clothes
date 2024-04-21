package com.grinko.autoclothes.util;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.Duration;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static com.grinko.autoclothes.util.LangUtil.nvl;
import static java.util.Objects.isNull;

@Slf4j
public class LocalCache<K, V> {
    private final LoadingCache<K, V> cache;


    public LocalCache(final int maxSize,
                      final Duration expireAfter,
                      final Function<K, V> loader) {

        cache = CacheBuilder.newBuilder()
            .maximumSize(maxSize)
            .expireAfterWrite(expireAfter.getMillis(), TimeUnit.MILLISECONDS)
            .build(
                new CacheLoader<>() {
                    @Override
                    public V load(final K key) {
                        return nvl(key, k -> loader.apply(key));
                    }
                }
            );
    }

    public void close() {

        cache.cleanUp();
    }

    public V get(final K key) {

        try {
            if (isNull(key)) {
                return null;
            } else {
                return cache.get(key);
            }
        } catch (final Exception e) {
            log.trace("cache error for "
                    + nvl(key, String::valueOf, "nil"),
                e.getMessage());
            return null;
        }
    }
}
