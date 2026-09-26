package org.example;

/**
 * Бросается, когда сумма операции некорректна
 * (отрицательная или нулевая).
 */
public class InvalidAmountException extends RuntimeException
{
    public InvalidAmountException(String message)
    {
        super(message);
    }
}