package com.grinko.autoclothes.util;

import lombok.val;
import org.apache.commons.io.IOUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;

import static com.grinko.autoclothes.util.ExceptionUtil.wrapRuntimeIfNeeded;
import static java.lang.Thread.currentThread;
import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.StringUtils.remove;
import static org.apache.commons.lang3.StringUtils.trim;
import static org.joda.time.Duration.standardMinutes;

public final class DbUtil {

    private static final LocalCache<String, String> SQL_CACHE = new LocalCache<>(
        100,
        standardMinutes(60),
        key -> {
            val cl = currentThread().getContextClassLoader();
            try (InputStream in = cl.getResourceAsStream(key)) {
                return IOUtils.toString(requireNonNull(in), StandardCharsets.UTF_8);
            } catch (Exception e) {
                throw wrapRuntimeIfNeeded(e);
            }
        }
    );

    private DbUtil() {
    }

    /**
     * withinTx
     *
     * @param template - template
     * @param action   - action
     */
    public static void withinTx(final TransactionTemplate template,
                                final Action0 action) {

        template.execute(
            status -> {
                action.run();
                return 0;
            }
        );
    }

    public static TransactionTemplate requiresNewTx(final PlatformTransactionManager txManager) {

        val template = new TransactionTemplate(txManager);
        template.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        return template;
    }

    public static Function<String, String> sqls(final String resource) {

        val res = trim(resource);

        return id -> {
            val key = "db/sql/" + res + "/" + trim(remove(id, ".sql")) + ".sql";
            return SQL_CACHE.get(key);
        };
    }

}
