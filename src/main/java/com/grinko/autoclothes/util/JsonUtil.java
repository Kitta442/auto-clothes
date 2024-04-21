package com.grinko.autoclothes.util;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.val;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static com.grinko.autoclothes.util.LangUtil.ifNonBlank;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;
import static java.util.Collections.emptyList;


public final class JsonUtil {

    public static final DateTimeFormatter DATE_TIME_FORMAT =
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss[.SSS]");

    private JsonUtil() {
    }

    public static void node2arr(final JsonNode root,
                                final String fname,
                                final Consumer<List<String>> consumer) {

        if (root.hasNonNull(fname)) {
            val v = root.path(fname);
            if (v.isArray()) {
                val l = new ArrayList<String>();
                v.elements().forEachRemaining(i -> l.add(i.asText()));
                consumer.accept(l);
            }
        } else {
            consumer.accept(emptyList());
        }
    }

    public static void node2int(final JsonNode root,
                                final String fname,
                                final Consumer<Integer> consumer) {

        if (root.hasNonNull(fname)) {
            val v = root.path(fname);
            if (v.isNumber()) {
                consumer.accept(v.asInt());
            } else {
                consumer.accept(parseInt(v.asText()));
            }
        }
    }

    public static void node2long(final JsonNode root,
                                 final String fname,
                                 final Consumer<Long> consumer) {

        if (root.hasNonNull(fname)) {
            val v = root.path(fname);
            if (v.isNumber()) {
                consumer.accept(v.asLong(Long.MAX_VALUE));
            } else {
                consumer.accept(parseLong(v.asText()));
            }
        }
    }

    public static void node2str(final JsonNode root,
                                final String fname,
                                final Consumer<String> consumer) {

        if (root.hasNonNull(fname)) {
            ifNonBlank(root.path(fname).asText(), consumer);
        }
    }

    public static void node2time(final JsonNode root,
                                 final String fname,
                                 final Consumer<LocalDateTime> consumer) {

        node2str(
            root,
            fname,
            str -> consumer.accept(LocalDateTime.parse(str, DATE_TIME_FORMAT))
        );
    }
}
