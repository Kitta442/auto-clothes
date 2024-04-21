package com.grinko.autoclothes.util;

import lombok.val;
import org.apache.commons.lang3.RegExUtils;
import org.joda.time.Duration;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.grinko.autoclothes.util.LangUtil.nvl;
import static com.grinko.autoclothes.util.StringUtil.str2int;
import static org.apache.commons.lang3.StringUtils.isNumeric;
import static org.apache.commons.lang3.StringUtils.lowerCase;
import static org.joda.time.Duration.millis;
import static org.joda.time.Duration.standardDays;
import static org.joda.time.Duration.standardHours;
import static org.joda.time.Duration.standardMinutes;
import static org.joda.time.Duration.standardSeconds;


/**
 * Handy methods related to date time formatting.
 */
public final class DtUtil {

    public static final LocalDateTime BEGIN_OF_ERA = LocalDateTime.of(
        LocalDate.of(1900, 1, 1),
        LocalTime.of(0, 0, 0)
    );
    public static final DateTimeFormatter WEB_TIME_FMT = DateTimeFormatter
        .ofPattern("dd.MM.yyyy HH:mm:ss");
    public static final DateTimeFormatter KAFKA_TIME_FMT = DateTimeFormatter
        .ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
    public static final DateTimeFormatter KAFKA_TIME_PARSER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    public static final DateTimeFormatter WEB_DATE_FMT = DateTimeFormatter
        .ofPattern("dd.MM.yyyy");
    public static final ZoneId DEF_TZ = ZoneId.of("Europe/Moscow");
    public static final ZoneOffset DEF_OFFSET = DEF_TZ.getRules().getOffset(Instant.now());
    private static final Pattern DAY_PATTERN = Pattern.compile("(\\d+)(day|days)");
    private static final Pattern HRS_PATTERN = Pattern.compile("(\\d+)(hr|hrs)");
    private static final Pattern MIN_PATTERN = Pattern.compile("(\\d+)(min|mins)");
    private static final Pattern SEC_PATTERN = Pattern.compile("(\\d+)?(sec|secs)");
    private static final Pattern MILLIS_PATTERN = Pattern.compile("(\\d+)?(ms)");

    private DtUtil() {
    }

    public static LocalDateTime beginOfDay(final LocalDate date) {
        return LocalDateTime.of(
            date,
            LocalTime.of(0, 0, 0)
        );
    }

    public static LocalDateTime endOfDay(final LocalDate date) {
        return LocalDateTime.of(
            date,
            LocalTime.of(23, 59, 59)
        );
    }

    public static LocalDateTime beginOfYear(final LocalDateTime dateTime) {
        return LocalDateTime.of(
            dateTime.getYear(),
            1,
            1,
            0,
            0
        );
    }

    public static OffsetDateTime toOffsetDateTime(final LocalDateTime dt) {

        return nvl(dt, d -> d.atOffset(DEF_OFFSET));
    }

    public static OffsetDateTime toOffsetDateTime(final Timestamp ts) {

        return nvl(
            ts,
            t -> OffsetDateTime.ofInstant(t.toInstant(), DEF_TZ)
        );
    }

    public static LocalDateTime toLocalDateTime(final Timestamp ts) {

        return nvl(
            ts,
            Timestamp::toLocalDateTime
        );
    }


    public static Duration humanDuration(final String seq) {

        val res = new MutableHolder<>(Duration.ZERO);

        final String text = lowerCase(RegExUtils.removeAll(nvl(seq, ""), " "));
        final Matcher days = DAY_PATTERN.matcher(text);
        if (days.find()) {
            res.modify(i -> i.plus(standardDays(parseGroup(days))));
        }
        val hrs = HRS_PATTERN.matcher(text);
        if (hrs.find()) {
            res.modify(i -> i.plus(standardHours(parseGroup(hrs))));
        }
        val min = MIN_PATTERN.matcher(text);
        if (min.find()) {
            res.modify(i -> i.plus(standardMinutes(parseGroup(min))));
        }
        val sec = SEC_PATTERN.matcher(text);
        if (sec.find()) {
            res.modify(i -> i.plus(standardSeconds(parseGroup(sec))));
        }
        val ms = MILLIS_PATTERN.matcher(text);
        if (ms.find()) {
            res.modify(i -> i.plus(millis(parseGroup(ms))));
        }

        return res.get();
    }

    private static Integer parseGroup(final Matcher sec) {

        val text = sec.group(1);
        if (isNumeric(text)) {
            return str2int(sec.group(1));
        }
        return 0;
    }

    public static Timestamp toTimestamp(final LocalDate date) {

        return nvl(
            date,
            d -> Timestamp.valueOf(d.atTime(LocalTime.MIDNIGHT))
        );
    }

    public static Timestamp toTimestamp(final LocalDateTime date) {

        return nvl(
            date,
            Timestamp::valueOf
        );
    }

}