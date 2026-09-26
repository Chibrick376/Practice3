package org.example;

/**
 * Результат одной банковской операции.
 */
public record Transaction(
        TransactionType type,
        AccountNumber account,
        double amount,
        TransactionStatus status)
{
}