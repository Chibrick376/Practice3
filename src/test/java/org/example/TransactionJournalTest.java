package org.example;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TransactionJournalTest
{

    private Transaction tx(String number, double amount)
    {
        return new Transaction(
                TransactionType.TRANSFER,
                new AccountNumber(number),
                amount,
                TransactionStatus.SUCCESS
        );
    }

    @Test
    void transactionsAreStoredInOrder()
    {
        TransactionJournal journal = new TransactionJournal();
        Transaction first = tx("0000000001", 100);
        Transaction second = tx("0000000002", 200);
        Transaction third = tx("0000000003", 300);

        journal.add(first);
        journal.add(second);
        journal.add(third);

        List<Transaction> all = journal.findAll();
        assertSame(first, all.get(0));
        assertSame(second, all.get(1));
        assertSame(third, all.get(2));
    }

    @Test
    void duplicateTransactionsAreAllowed()
    {
        TransactionJournal journal = new TransactionJournal();
        Transaction tx = tx("0000000004", 500);

        journal.add(tx);
        journal.add(tx);

        assertEquals(2, journal.size());
    }

    @Test
    void sizeMatchesNumberOfEntries()
    {
        TransactionJournal journal = new TransactionJournal();
        journal.add(tx("0000000005", 100));
        journal.add(tx("0000000006", 200));

        assertEquals(2, journal.size());
    }

    @Test
    void returnedListDoesNotExposeInternalState()
    {
        TransactionJournal journal = new TransactionJournal();
        journal.add(tx("0000000007", 100));

        List<Transaction> copy = journal.findAll();
        copy.clear();

        assertEquals(1, journal.size());
    }
}