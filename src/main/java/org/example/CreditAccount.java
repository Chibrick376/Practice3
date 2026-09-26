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
            throw new IllegalArgumentException("Credit limit must not be negative");

        this.creditLimit = creditLimit;
    }

    public double getCreditLimit()
    {
        return creditLimit;
    }

    @Override
    public void withdraw(double amount)
    {
        if (amount <= 0)
            throw new IllegalArgumentException("Amount must be positive");

        if (getBalance() - amount < -creditLimit)
            throw new InsufficientFundsException("Insufficient funds");

        decreaseBalance(amount);
    }
}