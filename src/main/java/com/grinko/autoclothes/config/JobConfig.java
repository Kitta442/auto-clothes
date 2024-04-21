package com.grinko.autoclothes.config;

import com.grinko.autoclothes.job.JobFactory;
import com.grinko.autoclothes.util.JobService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.time.Duration;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import static com.grinko.autoclothes.util.FixedDelayJobService.fixedDelayJobService;
import static java.lang.Math.max;
import static java.util.concurrent.Executors.newScheduledThreadPool;

@Configuration
@Setter
@Getter
@Slf4j
@ConfigurationProperties(prefix = "async-jobs", ignoreUnknownFields = false)
public class JobConfig {

    private int concurrency;

    @Bean(destroyMethod = "shutdown")
    @Lazy
    ScheduledExecutorService jobExecutor() {

        val executor = (ScheduledThreadPoolExecutor) newScheduledThreadPool(
            max(concurrency, 1),
            new BasicThreadFactory.Builder()
                .namingPattern("sample-job-%d")
                .daemon(true)
                .priority(Thread.MAX_PRIORITY)
                .uncaughtExceptionHandler((t, e) -> log.error("default uncaught execution {}", t, e))
                .build()
        );

        executor.setRemoveOnCancelPolicy(true);
        executor.setRejectedExecutionHandler(
            (r, e) -> log.error("default rejected execution {} remove: {}", e, e.getQueue().remove(r))
        );
        return executor;
    }

    @Bean(destroyMethod = "cancel")
    JobService.ScheduledJob startSampleJob(final ScheduledExecutorService jobExecutor,
                                           final JobFactory jobFactory) {

        return fixedDelayJobService(jobExecutor)
            .schedule(
                "ClothJob",
                jobFactory::clothJob,
                Duration.ofMinutes(2)
            );

    }
}

