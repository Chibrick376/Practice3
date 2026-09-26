package org.example;

/**
 * Базовый абстрактный класс банковского счёта.
 * Хранит номер, владельца и баланс.
 * Напрямую менять баланс снаружи нельзя — только через методы.
 */
public abstract class BankAccount
{

    private final String number;
    private final String owner;
    private double balance;

    protected BankAccount(String number, String owner, double initialBalance)
    {
        if (number == null || number.trim().isEmpty())
            throw new IllegalArgumentException("Номер счёта не может быть пустым!");
        if (owner == null || owner.trim().isEmpty())
            throw new IllegalArgumentException("Имя владельца не может быть пустым!");
        if (initialBalance < 0)
            throw new IllegalArgumentException("Начальный баланс не может быть отрицательным!");

        this.number = number;
        this.owner = owner;
        this.balance = initialBalance;
    }

    public String getNumber()
    {
        return number;
    }

    public String getOwner()
    {
        return owner;
    }

    public double getBalance()
    {
        return balance;
    }

    public void deposit(double amount)
    {
        if (amount <= 0)
            return;
        balance = balance + amount;
    }

    public abstract boolean withdraw(double amount);

    protected void setBalance(double newBalance)
    {
        this.balance = newBalance;
    }

    @Override
    public String toString()
    {
        return getClass().getSimpleName() + "{" +
                "number='" + number + '\'' +
                ", owner='" + owner + '\'' +
                ", balance=" + balance +
                '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        BankAccount that = (BankAccount) o;
        return number.equals(that.number);
    }

    @Override
    public int hashCode()
    {
        return number.hashCode();
    }
}