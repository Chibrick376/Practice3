package org.example;

/**
 * Комиссия отсутствует.
 * Для любой суммы возвращает 0.
 */
public class NoCommission implements CommissionPolicy
{

    @Override
    public double calculate(double amount)
    {
        return 0;
    }
}