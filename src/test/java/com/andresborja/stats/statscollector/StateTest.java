package com.andresborja.stats.statscollector;

import static com.andresborja.stats.statscollector.TimeUtils.TIME_WINDOW_IN_SECS;
import static com.andresborja.stats.statscollector.TimeUtils.getCurrentEpocTimestamp;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Unit tests for the State object.
 * @author borjaa
 */
public class StateTest {

    private static final Transaction TRANSACTION1 = new Transaction(10, getCurrentEpocTimestamp() - 100);
    private static final Transaction TRANSACTION2 = new Transaction(20, getCurrentEpocTimestamp() - 200);
    private static final Transaction TRANSACTION3 = new Transaction(30, getCurrentEpocTimestamp() - 300);
    private static final Transaction TRANSACTION4 = new Transaction(40, getCurrentEpocTimestamp() - 150);
    private static final Transaction TRANSACTION_OLD = new Transaction(10, getCurrentEpocTimestamp() - (TIME_WINDOW_IN_SECS + 1) * 1000);

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private State state;

    @Before
    public void setUp() throws Exception {
        state = new State();
    }

    @Test
    public void testAdd() throws InvalidTransactionTimeException {
        state.add(TRANSACTION1);
        state.add(TRANSACTION2);
        state.add(TRANSACTION3);
        state.add(TRANSACTION4);
        assertEquals(25d, state.getAvg(), 0);
        assertEquals(100d, state.getSum(), 0);
        assertEquals(4, state.getCount());
        assertEquals(40d, state.getMax(), 0);
        assertEquals(10d, state.getMin(), 0);
    }

    @Test
    public void testAddOverRank() throws InvalidTransactionTimeException {
        state.add(TRANSACTION1);
        exception.expect(InvalidTransactionTimeException.class);
        state.add(TRANSACTION_OLD);
        fail("Expecting exception");
    }

    @Test
    public void testAddNoData() throws InvalidTransactionTimeException {
        assertEquals(0, state.getAvg(), 0);
        assertEquals(0, state.getSum(), 0);
        assertEquals(0, state.getCount());
        assertEquals(0, state.getMax(), 0);
        assertEquals(0, state.getMin(), 0);
    }

    @Test
    public void testRemove() throws InvalidTransactionTimeException {
        state.add(TRANSACTION1);
        state.add(TRANSACTION2);
        state.add(TRANSACTION3);
        state.add(TRANSACTION4);
        state.remove(TRANSACTION3);
        assertEquals(70d/3d, state.getAvg(), 0);
        assertEquals(70d, state.getSum(), 0);
        assertEquals(3, state.getCount());
        assertEquals(40d, state.getMax(), 0);
        assertEquals(10d, state.getMin(), 0);
    }

    @Test
    public void testRemoveMax() throws InvalidTransactionTimeException {
        state.add(TRANSACTION1);
        state.add(TRANSACTION2);
        state.add(TRANSACTION3);
        state.add(TRANSACTION4);
        state.remove(TRANSACTION4);
        assertEquals(60d/3d, state.getAvg(), 0);
        assertEquals(60d, state.getSum(), 0);
        assertEquals(3, state.getCount());
        assertEquals(30d, state.getMax(), 0);
        assertEquals(10d, state.getMin(), 0);
    }

    @Test
    public void testRemoveMin() throws InvalidTransactionTimeException {
        state.add(TRANSACTION1);
        state.add(TRANSACTION2);
        state.add(TRANSACTION3);
        state.add(TRANSACTION4);
        state.remove(TRANSACTION1);
        assertEquals(30d, state.getAvg(), 0);
        assertEquals(90d, state.getSum(), 0);
        assertEquals(3, state.getCount());
        assertEquals(40d, state.getMax(), 0);
        assertEquals(20d, state.getMin(), 0);
    }

    @Test
    public void testAddAndRemoveAll() throws InvalidTransactionTimeException {
        state.add(TRANSACTION1);
        state.add(TRANSACTION2);
        state.add(TRANSACTION3);
        state.add(TRANSACTION4);
        state.remove(TRANSACTION1);
        state.remove(TRANSACTION2);
        state.remove(TRANSACTION3);
        state.remove(TRANSACTION4);
        assertEquals(0, state.getAvg(), 0);
        assertEquals(0, state.getSum(), 0);
        assertEquals(0, state.getCount());
        assertEquals(0, state.getMax(), 0);
        assertEquals(0, state.getMin(), 0);
    }

    @Test
    public void testAddAndRemoveAllMixed() throws InvalidTransactionTimeException {
        state.add(TRANSACTION1);
        state.add(TRANSACTION2);
        state.add(TRANSACTION3);
        state.remove(TRANSACTION1);
        state.remove(TRANSACTION2);
        state.add(TRANSACTION4);
        state.remove(TRANSACTION3);
        state.remove(TRANSACTION4);
        assertEquals(0, state.getAvg(), 0);
        assertEquals(0, state.getSum(), 0);
        assertEquals(0, state.getCount());
        assertEquals(0, state.getMax(), 0);
        assertEquals(0, state.getMin(), 0);
    }

    @Test
    public void testRemoveTxNotAdded() throws InvalidTransactionTimeException {
        exception.expect(InvalidTransactionTimeException.class);
        state.remove(TRANSACTION1);
    }

}
