package org.example;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TransactionStatisticsTest
{

    private final TransactionStatistics stats = new TransactionStatistics();

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
    void emptyListReturnsEmptyMaps()
    {
        List<Transaction> list = new ArrayList<>();

        assertTrue(stats.countByAccount(list).isEmpty());
        assertTrue(stats.totalAmountByAccount(list).isEmpty());
    }

    @Test
    void singleAccountCountAndTotal()
    {
        List<Transaction> list = new ArrayList<>();
        list.add(tx("0000000001", 100));
        list.add(tx("0000000001", 200));

        Map<AccountNumber, Integer> counts = stats.countByAccount(list);
        Map<AccountNumber, Double> totals = stats.totalAmountByAccount(list);

        AccountNumber id = new AccountNumber("0000000001");
        assertEquals(2, counts.get(id));
        assertEquals(300.0, totals.get(id));
    }

    @Test
    void multipleAccountsAreSeparated()
    {
        List<Transaction> list = new ArrayList<>();
        list.add(tx("0000000001", 100));
        list.add(tx("0000000002", 200));
        list.add(tx("0000000001", 50));

        Map<AccountNumber, Integer> counts = stats.countByAccount(list);
        Map<AccountNumber, Double> totals = stats.totalAmountByAccount(list);

        assertEquals(2, counts.get(new AccountNumber("0000000001")));
        assertEquals(1, counts.get(new AccountNumber("0000000002")));

        assertEquals(150.0, totals.get(new AccountNumber("0000000001")));
        assertEquals(200.0, totals.get(new AccountNumber("0000000002")));
    }

    @Test
    void equalAccountNumbersAreAggregated()
    {
        List<Transaction> list = new ArrayList<>();
        list.add(tx("1111111111", 100));
        list.add(tx("1111111111", 200));

        Map<AccountNumber, Integer> counts = stats.countByAccount(list);

        assertEquals(1, counts.size());
        assertEquals(2, counts.get(new AccountNumber("1111111111")));
    }
}