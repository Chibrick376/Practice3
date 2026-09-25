package org.example;

/**
 * Политика расчёта комиссии за перевод.
 * Разные реализации могут считать комиссию по-разному.
 */
public interface CommissionPolicy
{

    /**
     * Возвращает величину комиссии для указанной суммы перевода.
     */
    double calculate(double amount);
}