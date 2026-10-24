package org.example;

/**
 * Заявка на перевод между счетами.
 */
public record TransferRequest(
        AccountNumber from,
        AccountNumber to,
        double amount)
{
}