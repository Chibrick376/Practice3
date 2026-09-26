package org.example;

/**
 * Бросается, когда сумма перевода превышает установленный лимит.
 */
public class TransferLimitExceededException extends RuntimeException
{
    public TransferLimitExceededException(String message)
    {
        super(message);
    }
}