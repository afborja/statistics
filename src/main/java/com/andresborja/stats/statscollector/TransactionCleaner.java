package com.andresborja.stats.statscollector;

import static com.andresborja.stats.statscollector.TimeUtils.isOldTransaction;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * Continuous thread to remove old transactions and update state.
 * For a more scalable implementation this component could run from different nodes, reading the next element to review
 * from a shared stack.
 * @author afborja
 */
@Slf4j
public class TransactionCleaner implements Runnable {

    private static boolean running = false;

    private final State state;

    @Inject
    public TransactionCleaner(@NonNull @Named("state") final State state) {
        this.state = state;
    }

    @Override
    public void run() {
        while(running) {
            try {
                removeOldTransactions();
            } catch (InvalidTransactionTimeException e) {
                e.printStackTrace();
            }
        }
    }

    public void start() {
        log.info("Starting the cleaner thread. [{}]...", System.currentTimeMillis());
        running = true;
        new Thread(this).start();
    }

    public void stop() {
        running = false;
    }

    private void removeOldTransactions() throws InvalidTransactionTimeException {
        List<Transaction> transactions = state.getTransactions();
        int size = transactions.size();
        Transaction toRemove = null;
        log.debug("Checking to remove from {} transactions...", transactions.size());
        for (int i = 0; i < size; i++) {
            Transaction tx = transactions.get(i);
            if (isOldTransaction(tx.getTimestamp())) {
                log.info("Removing old transaction [{}]...", System.currentTimeMillis());
                toRemove = tx;
                break;
            }
        }
        if (toRemove != null) {
            state.remove(toRemove);
        }
    }
}
