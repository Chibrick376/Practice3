package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTest
{

    @Test
    void transactionStoresAllFields()
    {
        Transaction tx = new Transaction(
                TransactionType.DEPOSIT,
                new AccountNumber("1234567890"),
                5000,
                TransactionStatus.SUCCESS
        );

        assertEquals(TransactionType.DEPOSIT, tx.type());
        assertEquals(new AccountNumber("1234567890"), tx.account());
        assertEquals(5000, tx.amount());
        assertEquals(TransactionStatus.SUCCESS, tx.status());
    }

    @Test
    void transactionToStringIsReadable()
    {
        Transaction tx = new Transaction(
                TransactionType.DEPOSIT,
                new AccountNumber("1234567890"),
                5000,
                TransactionStatus.SUCCESS
        );

        String text = tx.toString();

        assertTrue(text.contains("DEPOSIT"));
        assertTrue(text.contains("1234567890"));
        assertTrue(text.contains("5000.0"));
        assertTrue(text.contains("SUCCESS"));
    }

    @Test
    void rejectedTransactionHasRejectedStatus()
    {
        Transaction tx = new Transaction(
                TransactionType.TRANSFER,
                new AccountNumber("1234567890"),
                1000,
                TransactionStatus.REJECTED
        );

        assertEquals(TransactionStatus.REJECTED, tx.status());
    }

    @Test
    void equalTransactionsAreEqual()
    {
        Transaction a = new Transaction(
                TransactionType.WITHDRAWAL,
                new AccountNumber("1234567890"),
                300,
                TransactionStatus.SUCCESS
        );
        Transaction b = new Transaction(
                TransactionType.WITHDRAWAL,
                new AccountNumber("1234567890"),
                300,
                TransactionStatus.SUCCESS
        );

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }
}