package com.andresborja.stats.statscollector;

/**
 * Exception thrown when the incoming transaction is old or in the future.
 * @author afborja
 */
public class InvalidTransactionTimeException extends Exception {
    private static final long serialVersionUID = 1L;

    public InvalidTransactionTimeException(String msg) {
        super(msg);
    }
}
