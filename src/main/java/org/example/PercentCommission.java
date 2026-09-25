package org.example;

/**
 * Процентная комиссия.
 * Процент задаётся в конструкторе (например, 1 означает 1%).
 */
public class PercentCommission implements CommissionPolicy
{

    private final double percent;

    public PercentCommission(double percent)
    {
        if (percent < 0)
            throw new IllegalArgumentException("Процент комиссии не может быть отрицательным!");

        this.percent = percent;
    }

    public double getPercent()
    {
        return percent;
    }

    @Override
    public double calculate(double amount)
    {
        return amount * percent / 100.0;
    }
}
