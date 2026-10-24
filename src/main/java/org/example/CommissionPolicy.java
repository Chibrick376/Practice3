package org.example;

/**
 * Политика расчёта комиссии за перевод.
 */
@FunctionalInterface
public interface CommissionPolicy
{
    double calculate(double amount);
}