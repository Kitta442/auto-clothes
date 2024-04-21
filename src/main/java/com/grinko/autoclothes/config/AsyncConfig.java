package com.grinko.autoclothes.config;

import com.grinko.autoclothes.qualifiers.CommonAsyncExecutor;
import com.grinko.autoclothes.util.WaitedAsyncExecutor;
import com.grinko.autoclothes.util.WaitedExecutor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.time.Duration;

import static java.util.concurrent.Executors.newFixedThreadPool;

@Getter
@Setter
@Configuration
public class AsyncConfig {

    @Bean(destroyMethod = "terminate")
    @Lazy
    @CommonAsyncExecutor
    WaitedExecutor commonAsyncExecutor() {
        return new WaitedAsyncExecutor(
            Duration.ofMinutes(20),
            newFixedThreadPool(128)
        );
    }
}
