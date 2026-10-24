package org.example;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Журнал транзакций. Хранит записи в порядке добавления
 * и использует индекс для быстрого поиска по счёту.
 */
public class TransactionJournal
{

    private final List<Transaction> transactions = new ArrayList<>();
    private final TransactionIndex index = new TransactionIndex();

    public void add(Transaction transaction)
    {
        transactions.add(transaction);
        index.add(transaction);
    }

    public List<Transaction> findAll()
    {
        return new ArrayList<>(transactions);
    }

    public List<Transaction> findByAccount(AccountNumber number)
    {
        return index.findByAccount(number);
    }

    public void removeRejected()
    {
        Iterator<Transaction> iterator = transactions.iterator();
        while (iterator.hasNext())
        {
            Transaction tx = iterator.next();
            if (tx.status() == TransactionStatus.REJECTED)
            {
                iterator.remove();
            }
        }
        // Обрати внимание: удаление из списка не обновляет индекс.
        // Если понадобится, здесь нужно будет добавить очистку индекса.
    }

    public int size()
    {
        return transactions.size();
    }
}