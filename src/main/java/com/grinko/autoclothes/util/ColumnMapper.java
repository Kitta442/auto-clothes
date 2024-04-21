package com.grinko.autoclothes.util;

import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.function.Consumer;

import static com.grinko.autoclothes.util.DtUtil.toLocalDateTime;
import static com.grinko.autoclothes.util.DtUtil.toOffsetDateTime;
import static org.apache.commons.lang3.BooleanUtils.toBoolean;
import static org.apache.commons.lang3.StringUtils.trim;

public class ColumnMapper {

    private final ResultSet rs;

    public ColumnMapper(final ResultSet rs) {
        this.rs = rs;
    }

    public static ColumnMapper columnMapper(final ResultSet rs) {

        return new ColumnMapper(rs);
    }

    @SneakyThrows
    public ColumnMapper asString(final String column,
                                 final Consumer<String> consumer) {

        consumer.accept(trim(rs.getString(column)));
        return this;
    }

    @SneakyThrows
    public ColumnMapper asBoolean(final String column,
                                  final Consumer<Boolean> consumer) {

        consumer.accept(toBoolean(rs.getObject(column, Boolean.class)));
        return this;
    }

    @SneakyThrows
    public ColumnMapper asInteger(final String column,
                                  final Consumer<Integer> consumer) {

        consumer.accept(rs.getObject(column, Integer.class));
        return this;
    }

    @SneakyThrows
    public ColumnMapper asLong(final String column,
                               final Consumer<Long> consumer) {

        consumer.accept(rs.getObject(column, Long.class));
        return this;
    }

    @SneakyThrows
    public ColumnMapper asOffsetDateTime(final String column,
                                         final Consumer<OffsetDateTime> consumer) {

        consumer.accept(toOffsetDateTime(rs.getTimestamp(column)));
        return this;
    }

    @SneakyThrows
    public ColumnMapper asLocalDateTime(final String column,
                                        final Consumer<LocalDateTime> consumer) {

        consumer.accept(toLocalDateTime(rs.getTimestamp(column)));
        return this;
    }

}
