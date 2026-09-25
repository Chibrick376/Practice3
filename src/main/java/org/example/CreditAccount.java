package org.example;

/**
 * Кредитный счёт.
 * Отличается тем, что может уходить в минус,
 * но не глубже, чем на величину кредитного лимита.
 */
public class CreditAccount extends BankAccount
{

    private final double creditLimit;

    public CreditAccount(String number,
                         String owner,
                         double initialBalance,
                         double creditLimit)
    {
        super(number, owner, initialBalance);

        if (creditLimit < 0)
            throw new IllegalArgumentException("Кредитный лимит не может быть отрицательным!");

        this.creditLimit = creditLimit;
    }

    public double getCreditLimit()
    {
        return creditLimit;
    }

    /**
     * Снять деньги можно, даже если баланс уйдёт в минус,
     * но не глубже -creditLimit.
     */
    @Override
    public boolean withdraw(double amount)
    {
        // Отрицательные и нулевые суммы снимать нельзя
        if (amount <= 0)
            return false;

        // Считаем, каким станет баланс после снятия
        double afterWithdraw = getBalance() - amount;

        // Если уйдём ниже -creditLimit — отказ
        if (afterWithdraw < -creditLimit)
            return false;

        // Всё ок — обновляем баланс (может стать отрицательным)
        setBalance(afterWithdraw);
        return true;
    }
}