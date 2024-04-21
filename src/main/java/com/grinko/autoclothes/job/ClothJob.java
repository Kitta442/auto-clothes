package com.grinko.autoclothes.job;

import com.grinko.autoclothes.util.Action0;
import com.grinko.autoclothes.util.WaitedExecutor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Supplier;

import static com.grinko.autoclothes.util.AuxUtil.interruptibleSleep;
import static java.lang.Math.random;

@Slf4j
@RequiredArgsConstructor
public class ClothJob implements Action0 {

    private final WaitedExecutor executor;
    private final Supplier<Integer> initialValue;

    @Override
    public void run() {

        log.info("initial value {}", initialValue.get());
        var coreSignal = coreTask();

        executor.execute(
            () -> dependentTask(coreSignal),
            () -> dependentTask(coreSignal)
        ).await();
    }

    private WaitedExecutor.Signal coreTask() {

        return executor.execute(
            this::someDurableTask,
            this::someDurableTask,
            this::someDurableTask
        );
    }

    private void dependentTask(final WaitedExecutor.Signal signal) {

        signal.awaitAndThen(
            this::someDurableTask
        );

    }

    private void someDurableTask() {
        var random = 1 + (long) (random() * (1000L - 1));
        interruptibleSleep(random);
    }
}
