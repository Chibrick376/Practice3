package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionTest
{

    @Test
    void insufficientFundsExceptionHasMessage()
    {
        InsufficientFundsException ex =
                new InsufficientFundsException("Insufficient funds");
        assertEquals("Insufficient funds", ex.getMessage());
    }

    @Test
    void insufficientFundsExceptionIsRuntimeException()
    {
        InsufficientFundsException ex =
                new InsufficientFundsException("test");
        assertTrue(ex instanceof RuntimeException);
    }

    @Test
    void transferLimitExceededExceptionHasMessage()
    {
        TransferLimitExceededException ex =
                new TransferLimitExceededException("Transfer limit exceeded");
        assertEquals("Transfer limit exceeded", ex.getMessage());
    }

    @Test
    void transferLimitExceededExceptionIsRuntimeException()
    {
        TransferLimitExceededException ex =
                new TransferLimitExceededException("test");
        assertTrue(ex instanceof RuntimeException);
    }

    @Test
    void invalidAmountExceptionHasMessage()
    {
        InvalidAmountException ex =
                new InvalidAmountException("Amount must be positive");
        assertEquals("Amount must be positive", ex.getMessage());
    }

    @Test
    void invalidAmountExceptionIsRuntimeException()
    {
        InvalidAmountException ex =
                new InvalidAmountException("test");
        assertTrue(ex instanceof RuntimeException);
    }
}