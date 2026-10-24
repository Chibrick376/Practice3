package org.example;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TransactionJournalTest
{

    private Transaction tx(String number, double amount, TransactionStatus status)
    {
        return new Transaction(
                TransactionType.TRANSFER,
                new AccountNumber(number),
                amount,
                status
        );
    }

    @Test
    void transactionsAreStoredInOrder()
    {
        TransactionJournal journal = new TransactionJournal();
        Transaction first = tx("0000000001", 100, TransactionStatus.SUCCESS);
        Transaction second = tx("0000000002", 200, TransactionStatus.SUCCESS);
        Transaction third = tx("0000000003", 300, TransactionStatus.SUCCESS);

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
        Transaction tx = tx("0000000004", 500, TransactionStatus.SUCCESS);

        journal.add(tx);
        journal.add(tx);

        assertEquals(2, journal.size());
    }

    @Test
    void sizeMatchesNumberOfEntries()
    {
        TransactionJournal journal = new TransactionJournal();
        journal.add(tx("0000000005", 100, TransactionStatus.SUCCESS));
        journal.add(tx("0000000006", 200, TransactionStatus.SUCCESS));

        assertEquals(2, journal.size());
    }

    @Test
    void returnedListDoesNotExposeInternalState()
    {
        TransactionJournal journal = new TransactionJournal();
        journal.add(tx("0000000007", 100, TransactionStatus.SUCCESS));

        List<Transaction> copy = journal.findAll();
        copy.clear();

        assertEquals(1, journal.size());
    }

    @Test
    void removeRejectedRemovesOnlyRejected()
    {
        TransactionJournal journal = new TransactionJournal();
        journal.add(tx("0000000001", 100, TransactionStatus.SUCCESS));
        journal.add(tx("0000000002", 200, TransactionStatus.REJECTED));
        journal.add(tx("0000000003", 300, TransactionStatus.SUCCESS));
        journal.add(tx("0000000004", 400, TransactionStatus.REJECTED));

        journal.removeRejected();

        assertEquals(2, journal.size());
        assertTrue(journal.findAll().stream()
                .allMatch(tx -> tx.status() == TransactionStatus.SUCCESS));
    }

    @Test
    void removeRejectedKeepsOrderOfRemaining()
    {
        TransactionJournal journal = new TransactionJournal();
        Transaction first = tx("0000000001", 100, TransactionStatus.SUCCESS);
        journal.add(first);
        journal.add(tx("0000000002", 200, TransactionStatus.REJECTED));
        Transaction third = tx("0000000003", 300, TransactionStatus.SUCCESS);
        journal.add(third);

        journal.removeRejected();

        List<Transaction> all = journal.findAll();
        assertEquals(2, all.size());
        assertSame(first, all.get(0));
        assertSame(third, all.get(1));
    }

    @Test
    void removeRejectedOnEmptyJournalDoesNothing()
    {
        TransactionJournal journal = new TransactionJournal();

        journal.removeRejected();

        assertEquals(0, journal.size());
    }

    @Test
    void removeRejectedWithNoRejectedKeepsAll()
    {
        TransactionJournal journal = new TransactionJournal();
        journal.add(tx("0000000001", 100, TransactionStatus.SUCCESS));
        journal.add(tx("0000000002", 200, TransactionStatus.SUCCESS));

        journal.removeRejected();

        assertEquals(2, journal.size());
    }
}