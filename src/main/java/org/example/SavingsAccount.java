package org.example;

public class SavingsAccount extends BankAccount
{

    private final double minimumBalance;

    public SavingsAccount(AccountNumber number,
                          String owner,
                          double initialBalance,
                          double minimumBalance)
    {
        super(number, owner, initialBalance);

        if (minimumBalance < 0)
            throw new IllegalArgumentException("Minimum balance must not be negative");
        if (initialBalance < minimumBalance)
            throw new IllegalArgumentException("Initial balance must be >= minimum balance");

        this.minimumBalance = minimumBalance;
    }

    public double getMinimumBalance()
    {
        return minimumBalance;
    }

    @Override
    public void withdraw(double amount)
    {
        if (amount <= 0)
            throw new IllegalArgumentException("Amount must be positive");

        if (getBalance() - amount < minimumBalance)
            throw new InsufficientFundsException("Insufficient funds");

        decreaseBalance(amount);
    }
}