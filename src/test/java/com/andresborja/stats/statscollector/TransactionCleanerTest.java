package com.andresborja.stats.statscollector;

import static com.andresborja.stats.statscollector.TimeUtils.getCurrentEpocTimestamp;

import static org.junit.Assert.assertEquals;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * Unit tests for the transaction cleaner thread.
 * @author afborja
 */
public class TransactionCleanerTest {

    private static final int SEVENTY_SECONDS = 70 * 1000;
    private static final Transaction TRANSACTION1 = new Transaction(10, getCurrentEpocTimestamp() - SEVENTY_SECONDS);
    private static final Transaction TRANSACTION2 = new Transaction(20, getCurrentEpocTimestamp() - SEVENTY_SECONDS);
    private static final Transaction TRANSACTION3 = new Transaction(30, getCurrentEpocTimestamp() - SEVENTY_SECONDS);
    private static final Transaction TRANSACTION4 = new Transaction(40, getCurrentEpocTimestamp() - SEVENTY_SECONDS);
    private static final Transaction TRANSACTION_IN_TIME = new Transaction(40, getCurrentEpocTimestamp() - 10);

    private TransactionCleaner transactionCleaner;
    private State state;
    private List<Transaction> transactions;

    @Before
    public void setUp() throws Exception {
        state = mock(State.class);
        transactions = new ArrayList<>(Arrays.asList(
                        TRANSACTION1,
                        TRANSACTION2,
                        TRANSACTION3,
                        TRANSACTION4,
                        TRANSACTION_IN_TIME));
        when(state.getTransactions()).thenReturn(transactions);
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                Transaction t = (Transaction) args[0];
                transactions.remove(t);
                return null;
            }
        }).when(state).remove(any());
        transactionCleaner = new TransactionCleaner(state);
    }

    @Test
    public void testRun() throws Exception {
        transactionCleaner.start();
        Thread.sleep(1000);
        transactionCleaner.stop();
        verify(state, times(4)).remove(any());
        assertEquals(1, transactions.size());
    }

}
