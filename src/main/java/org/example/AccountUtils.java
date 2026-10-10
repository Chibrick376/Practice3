package org.example;

import java.util.List;

/**
 * Утилиты для работы с банковскими счетами.
 */
public final class AccountUtils
{

    private AccountUtils()
    {
    }

    /**
     * Суммарный баланс любого списка счетов.
     */
    public static double totalBalance(List<? extends BankAccount> accounts)
    {
        double total = 0;
        for (BankAccount account : accounts)
        {
            total += account.getBalance();
        }
        return total;
    }

    /**
     * Возвращает счёт с максимальным балансом.
     * Список не может быть пустым.
     */
    public static <T extends BankAccount> T richest(List<T> accounts)
    {
        if (accounts == null || accounts.isEmpty())
            throw new IllegalArgumentException("Accounts list must not be empty");

        T best = accounts.get(0);
        for (T account : accounts)
        {
            if (account.getBalance() > best.getBalance())
                best = account;
        }
        return best;
    }
}