package com.grinko.autoclothes.util;

import lombok.extern.slf4j.Slf4j;
import lombok.val;

import static com.grinko.autoclothes.util.LangUtil.bvl;
import static com.grinko.autoclothes.util.LangUtil.ifNonNull;
import static com.grinko.autoclothes.util.LangUtil.nvl;
import static org.apache.commons.lang3.exception.ExceptionUtils.getRootCauseMessage;


@Slf4j
public final class ExceptionUtil {

    private ExceptionUtil() {
    }

    public static void panic(final String msg) {

        throw new IllegalStateException(msg);
    }

    public static void panic(final Throwable e) {
        throw wrapRuntimeIfNeeded(e);
    }

    public static RuntimeException wrapRuntimeIfNeeded(final Throwable toWrap) {

        return toWrap instanceof RuntimeException
            ? (RuntimeException) toWrap
            : new RuntimeException(
            nvl(
                toWrap,
                i -> bvl(
                    i.getMessage(),
                    bvl(
                        getRootCauseMessage(i),
                        nvl(
                            i.getClass(),
                            Class::getSimpleName,
                            "Unrecognized error"
                        )
                    )
                )
            ),
            toWrap
        );
    }

    /**
     * Creates short stack trace
     *
     * @param throwable - exception
     * @param size      - stack trace size
     * @return short stack trace
     */
    public static StringBuilder shortStack(final Throwable throwable,
                                           final int size) {

        val buf = new StringBuilder();
        ifNonNull(
            throwable,
            t -> ifNonNull(
                t.getStackTrace(),
                elements -> {
                    int i = 0;
                    for (; i < elements.length && i < size; i++) {
                        val e = elements[i];
                        buf.append('\t')
                            .append("at ")
                            .append(e.getClassName())
                            .append(".")
                            .append(e.getMethodName())
                            .append(":")
                            .append(e.getLineNumber())
                            .append('\n');
                    }
                    if (i < elements.length) {
                        buf.append('\t').append("...");
                    } else if (buf.length() > 0) {
                        buf.deleteCharAt(buf.length() - 1);
                    }
                }
            )
        );
        return buf;
    }

}
