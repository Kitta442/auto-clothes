package com.grinko.autoclothes.util;

import lombok.SneakyThrows;
import lombok.val;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.function.Consumer;

import static com.grinko.autoclothes.util.AuxUtil.doSilently;
import static com.grinko.autoclothes.util.ExceptionUtil.wrapRuntimeIfNeeded;
import static java.io.File.createTempFile;

public final class IoUtil {

    private static final String BUCKET_REGEX = "^/?.+/";

    private IoUtil() {
    }

    public static void withinTempFile(final Consumer<File> ifOk) {

        val file = new MutableHolder<File>();
        try {
            file.accept(createTempFile("cloth", "tmp"));
            file.ifNonNull(
                f -> {
                    f.deleteOnExit();
                    ifOk.accept(f);
                }
            );
        } catch (Exception e) {
            throw wrapRuntimeIfNeeded(e);
        } finally {
            file.ifNonNull(
                f -> doSilently(f::delete)
            );
        }
    }

    @SneakyThrows
    public static void asOutputStream(final File file,
                                      final Consumer<OutputStream> streamConsumer) {

        try (OutputStream out = Files.newOutputStream(file.toPath())) {
            streamConsumer.accept(out);
            out.flush();
        }
    }

    @SneakyThrows
    public static void asInputStream(final File file,
                                     final Consumer<InputStream> streamConsumer) {

        try (InputStream in = Files.newInputStream(file.toPath())) {
            streamConsumer.accept(in);
        }
    }

}
