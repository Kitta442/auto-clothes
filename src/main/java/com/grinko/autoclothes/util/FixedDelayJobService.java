package com.grinko.autoclothes.util;

import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.util.stream.Collectors.toList;

@Slf4j
public class FixedDelayJobService implements JobService {

    private static final ScheduledJob DUMMY_JOB = () -> {
        //nop
    };
    private static final int INITIAL_DELAY = 4096;
    private final ScheduledExecutorService executor;

    /**
     * constructor
     *
     * @param executor executor
     */
    public FixedDelayJobService(final ScheduledExecutorService executor) {
        this.executor = executor;
    }

    public static JobService fixedDelayJobService(final ScheduledExecutorService executor) {

        return new FixedDelayJobService(executor);
    }

    @Override
    public ScheduledJob schedule(final String jobName,
                                 final Runnable run,
                                 final Duration delay) {

        return schedule(jobName, List.of(run), delay);
    }

    @Override
    public ScheduledJob schedule(final String jobName,
                                 final List<Runnable> runs,
                                 final Duration delay) {

        val ms = delay.toMillis();
        if (ms > 0) {
            val list = runs
                .stream()
                .map(
                    r -> executor.scheduleWithFixedDelay(
                        AuxUtil.safeRun(r),
                        INITIAL_DELAY,
                        ms,
                        TimeUnit.MILLISECONDS
                    )
                ).collect(toList());

            log.info("job '{}' scheduled with concurrency {} and delay {} ms",
                jobName, runs.size(), ms);


            return () -> Try.run(() -> list.forEach(f -> f.cancel(false)))
                .onFailure(e -> log.error("error when stopping job {}: {}", jobName, e.getMessage()))
                .andFinally(() -> log.info("job {} stopped", jobName));
        } else {
            log.info("job '{}' is switched off", jobName);
        }

        return DUMMY_JOB;
    }
}