package com.grinko.autoclothes.job;

import com.grinko.autoclothes.util.Action0;
import com.grinko.autoclothes.util.MutableHolder;
import com.grinko.autoclothes.util.TxUtil;
import com.grinko.autoclothes.util.WaitedExecutor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JobFactory {

    private final TxUtil txUtil;

    private final WaitedExecutor executor;

    public Action0 clothJob() {

        var initialValue = new MutableHolder<Integer>();
        return new SomeInitialJob(initialValue)
            .andThen(
                txUtil.txAction(
                    new ClothJob(executor, initialValue)
                )
            );
    }
}
