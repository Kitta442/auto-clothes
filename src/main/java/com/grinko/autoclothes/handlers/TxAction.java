package com.grinko.autoclothes.handlers;

import com.grinko.autoclothes.util.Action0;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.support.TransactionTemplate;

import static com.grinko.autoclothes.util.DbUtil.withinTx;

@RequiredArgsConstructor
public class TxAction implements Action0 {

    private final TransactionTemplate txTemplate;
    private final Action0 nested;

    @Override
    public void run() {

        withinTx(txTemplate, nested);
    }
}
