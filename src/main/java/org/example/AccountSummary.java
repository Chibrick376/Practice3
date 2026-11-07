package org.example;

/**
 * Краткая проекция банковского счёта:
 * номер, тип и баланс без прочих деталей.
 */
public record AccountSummary(
        AccountNumber number,
        String type,
        double balance)
{
}
