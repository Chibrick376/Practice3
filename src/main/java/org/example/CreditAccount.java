package org.example;

public class CreditAccount extends BankAccount
{

    private final double creditLimit;

    public CreditAccount(AccountNumber number,
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

    @Override
    public boolean withdraw(double amount)
    {
        if (amount <= 0)
            return false;

        double afterWithdraw = getBalance() - amount;
        if (afterWithdraw < -creditLimit)
            return false;

        setBalance(afterWithdraw);
        return true;
    }
}