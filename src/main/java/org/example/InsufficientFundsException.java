package org.example;

/**
 * Бросается, когда на счёте недостаточно средств
 * для выполнения операции.
 */
public class InsufficientFundsException extends RuntimeException
{
    public InsufficientFundsException(String message)
    {
        super(message);
    }
}