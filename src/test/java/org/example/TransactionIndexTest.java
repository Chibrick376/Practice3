package org.example;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TransactionIndexTest
{

    private Transaction transactionFor(AccountNumber number)
    {
        return new Transaction(
                TransactionType.TRANSFER,
                number,
                100,
                TransactionStatus.SUCCESS
        );
    }

    @Test
    void transactionCanBeFoundByEqualAccountNumber()
    {
        TransactionIndex index = new TransactionIndex();
        AccountNumber stored = new AccountNumber("1111111111");
        AccountNumber lookup = new AccountNumber("1111111111");

        index.add(transactionFor(stored));

        assertEquals(1, index.findByAccount(lookup).size());
    }

    @Test
    void findByAccountReturnsEmptyListIfNotFound()
    {
        TransactionIndex index = new TransactionIndex();
        index.add(transactionFor(new AccountNumber("1111111111")));

        List<Transaction> result = index.findByAccount(new AccountNumber("2222222222"));

        assertTrue(result.isEmpty());
    }

    @Test
    void findByAccountReturnsCopyOfList()
    {
        TransactionIndex index = new TransactionIndex();
        AccountNumber number = new AccountNumber("1111111111");
        index.add(transactionFor(number));

        List<Transaction> copy = index.findByAccount(number);
        copy.clear();

        assertEquals(1, index.findByAccount(number).size());
    }
}