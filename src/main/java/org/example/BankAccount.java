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

    /**
     * Конструктор доступен только наследникам.
     */
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

    /**
     * Пополняем счёт. Если сумма <= 0 — просто ничего не делаем.
     */
    public void deposit(double amount)
    {
        if (amount <= 0)
            // Смысла что-то менять нет — отсекаем сразу
            return;
        balance = balance + amount;
    }

    /**
     * Абстрактный метод — каждый наследник сам решает,
     * как именно снимать деньги.
     *
     * @return true, если снятие прошло успешно, иначе false
     */
    public abstract boolean withdraw(double amount);

    /**
     * Позволяет наследникам менять баланс, не открывая поле наружу.
     */
    protected void setBalance(double newBalance)
    {
        this.balance = newBalance;
    }
}