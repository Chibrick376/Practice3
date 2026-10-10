package org.example;

import java.util.ArrayList;
import java.util.List;

/**
 * Журнал транзакций. Хранит записи в порядке добавления.
 */
public class TransactionJournal
{

    private final List<Transaction> transactions = new ArrayList<>();

    public void add(Transaction transaction)
    {
        transactions.add(transaction);
    }

    public List<Transaction> findAll()
    {
        return new ArrayList<>(transactions);
    }

    public int size()
    {
        return transactions.size();
    }
}