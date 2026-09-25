package org.example;

/**
 * Сберегательный счёт.
 * После снятия денег на балансе должен оставаться
 * неснижаемый остаток — minimumBalance.
 */
public class SavingsAccount extends BankAccount
{

    private final double minimumBalance;

    public SavingsAccount(String number,
                          String owner,
                          double initialBalance,
                          double minimumBalance)
    {
        super(number, owner, initialBalance);

        if (minimumBalance < 0)
            throw new IllegalArgumentException("Минимальный остаток не может быть отрицательным!");
        if (initialBalance < minimumBalance)
            throw new IllegalArgumentException(
                    "Начальный баланс не может быть меньше минимального остатка!");

        this.minimumBalance = minimumBalance;
    }

    public double getMinimumBalance()
    {
        return minimumBalance;
    }

    @Override
    public boolean withdraw(double amount)
    {
        if (amount <= 0)
            return false;

        // Не даём уйти ниже неснижаемого остатка
        double afterWithdraw = getBalance() - amount;
        if (afterWithdraw < minimumBalance)
            return false;

        setBalance(afterWithdraw);
        return true;
    }
}