package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Вторичный индекс для быстрого поиска транзакций по номеру счёта.
 */
public class TransactionIndex
{

    private final Map<AccountNumber, List<Transaction>> byAccount = new HashMap<>();

    public void add(Transaction tx)
    {
        AccountNumber number = tx.account();
        List<Transaction> list = byAccount.get(number);

        if (list == null)
        {
            list = new ArrayList<>();
            byAccount.put(number, list);
        }

        list.add(tx);
    }

    public List<Transaction> findByAccount(AccountNumber number)
    {
        List<Transaction> list = byAccount.get(number);

        if (list == null)
            return new ArrayList<>();

        return new ArrayList<>(list);
    }
}