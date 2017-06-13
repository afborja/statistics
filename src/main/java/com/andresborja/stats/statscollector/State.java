package com.andresborja.stats.statscollector;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.inject.Named;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;

/**
 * Specific state for statistics at any given time.
 * @author afborja
 */
@Getter
@Named("state")
public class State {

    private double sum = 0;
    private double avg = 0;
    private Double max = null;
    private Double min = null;
    private long count = 0;

    /**
     * Window of transactions of the last 60 seconds.
     * Most recent last.
     * For a more scalable solution, this component can be implemented in a physical store.
     */
    @JsonIgnore
    private List<Transaction> transactions = new ArrayList<Transaction>();

    public synchronized void add(Transaction t) throws InvalidTransactionTimeException {
        long timestamp = t.getTimestamp();
        TimeUtils.validateTransaction(timestamp);
        double amount = t.getAmount();

        increaseCount();
        increaseSum(amount);
        updateAvg();
        updateMaxAndMin(amount);
        insertNewTransaction(t, timestamp);
    }

    public synchronized void remove(Transaction t) throws InvalidTransactionTimeException {
        if (!transactions.contains(t)) { // This validation can be removed to improve performance.
            throw new InvalidTransactionTimeException("Transaction not found: " + t);
        }

        double amount = t.getAmount();

        decreaseCount();
        decreaseSum(amount);
        updateAvg();
        removeTransaction(t);
        updateMax(amount);
        updateMin(amount);
    }

    private void removeTransaction(Transaction t) {
        transactions.remove(t);
    }

    private void updateMaxAndMin(double amount) {
        if (max == null) {
            max = amount;
        } else if (amount > max) {
            this.max = amount;
        }
        if (min == null) {
            min = amount;
        } else if (amount < min) {
            this.min = amount;
        }
    }

    private void updateMax(double amount) {
        if (max == null) {
            this.max = amount;
        } else if (max == amount) {
            max = findNewMax();
        }
    }

    private double findNewMax() {
        final Comparator<Transaction> maxValueComparator = (t1, t2) -> Double.compare(t1.getAmount(), t2.getAmount());
        return getMatchValue(maxValueComparator);
    }

    private double findNewMin() {
        final Comparator<Transaction> maxValueComparator = (t1, t2) -> Double.compare(t2.getAmount(), t1.getAmount());
        return getMatchValue(maxValueComparator);
    }

    private double getMatchValue(final Comparator<Transaction> comparator) {
        if (transactions.size() == 0) {
            return 0;
        } else {
            return transactions.stream()
                .max(comparator)
                .get().getAmount();
        }
    }

    private void updateMin(double amount) {
        if (min == null) {
            this.min = amount;
        } else if (min == amount) {
            min = findNewMin();
        }
    }

    private void increaseCount() {
        count++;
    }

    private void decreaseCount() {
        count--;
    }

    private void updateAvg() {
        if (count == 0) {
            avg = 0;
        } else {
            avg = sum / count;
        }
    }

    private void insertNewTransaction(Transaction t, long timestamp) {
        transactions.add(t);
    }

    private void increaseSum(double amount) {
        sum += amount;
    }

    private void decreaseSum(double amount) {
        sum -= amount;
    }

    public double getMax() {
        return max == null? 0 : max;
    }

    public double getMin() {
        return min == null? 0 : min;
    }

}
