package org.example;

/**
 * Обычный дебетовый счёт.
 * Нельзя уйти в минус: если денег не хватает — отказ.
 */
public class DebitAccount extends BankAccount
{

    public DebitAccount(String number, String owner, double initialBalance)
    {
        super(number, owner, initialBalance);
    }

    @Override
    public boolean withdraw(double amount)
    {
        // Отрицательные и нулевые суммы снимать нельзя
        if (amount <= 0)
            return false;

        // Денег не хватает — операция не проходит
        if (amount > getBalance())
            return false;

        // Всё ок — уменьшаем баланс
        setBalance(getBalance() - amount);
        return true;
    }
}