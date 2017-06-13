package com.andresborja.stats.statscollector;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for the transaction.
 * @author afborja
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    private double amount;

    /**
     * Unix timeformat in milliseconds (EPOCH).
     */
    private long timestamp;

}
