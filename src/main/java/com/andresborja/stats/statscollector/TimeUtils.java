package com.andresborja.stats.statscollector;

import java.time.Instant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Utilities to handle time elements.
 * @author afborja
 */
@NoArgsConstructor(access=AccessLevel.PRIVATE)
@Slf4j
public final class TimeUtils {

    public static final int TIME_WINDOW_IN_SECS = 60;

    public static void validateTransaction(long timestamp) throws InvalidTransactionTimeException {
        long currentTime = getCurrentEpocTimestamp();
        long differenceInSeconds = (currentTime - timestamp) / 1000;
        log.info("Checking time. Current time {} to compare against {} ", currentTime, timestamp);
        if (differenceInSeconds < 0) {
            throw new InvalidTransactionTimeException("Transaction from the future");
        }
        if (differenceInSeconds > TIME_WINDOW_IN_SECS) {
            throw new InvalidTransactionTimeException("Old transaction");
        }
    }

    public static boolean isOldTransaction(long timestamp) throws InvalidTransactionTimeException {
        long currentTime = getCurrentEpocTimestamp();
        long differenceInSeconds = (currentTime - timestamp) / 1000;
        if (differenceInSeconds < 0) {
            throw new InvalidTransactionTimeException("Transaction from the future");
        }
        return differenceInSeconds > TIME_WINDOW_IN_SECS;
    }

    public static long getCurrentEpocTimestamp() {
	return Instant.now().toEpochMilli();
    }

}
