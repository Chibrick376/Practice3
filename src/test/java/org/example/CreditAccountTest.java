package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreditAccountTest
{

    @Test
    void exampleFromTaskWorks()
    {
        // Arrange
        CreditAccount account = new CreditAccount("C-001", "Виктор", 1_000, 5_000);

        // Act
        boolean first = account.withdraw(4_000);
        boolean second = account.withdraw(3_000);

        // Assert
        assertTrue(first);
        assertFalse(second);
        assertEquals(-3_000, account.getBalance());
    }

    @Test
    void canGoNegativeWithinLimit()
    {
        // Arrange
        CreditAccount account = new CreditAccount("C-002", "Алиса", 0, 5_000);

        // Act
        boolean result = account.withdraw(3_000);

        // Assert
        assertTrue(result);
        assertEquals(-3_000, account.getBalance());
    }

    @Test
    void canWithdrawExactlyToLimit()
    {
        // Arrange
        CreditAccount account = new CreditAccount("C-003", "Борис", 0, 1_000);

        // Act
        boolean result = account.withdraw(1_000);

        // Assert
        assertTrue(result);
        assertEquals(-1_000, account.getBalance());
    }

    @Test
    void cannotExceedCreditLimit()
    {
        // Arrange
        CreditAccount account = new CreditAccount("C-004", "Галина", 0, 1_000);

        // Act
        boolean result = account.withdraw(1_001);

        // Assert
        assertFalse(result);
        assertEquals(0, account.getBalance());
    }

    @Test
    void cannotWithdrawNegativeAmount()
    {
        // Arrange
        CreditAccount account = new CreditAccount("C-005", "Дарья", 1_000, 5_000);

        // Act
        boolean result = account.withdraw(-100);

        // Assert
        assertFalse(result);
        assertEquals(1_000, account.getBalance());
    }

    @Test
    void depositReducesDebt()
    {
        // Arrange
        CreditAccount account = new CreditAccount("C-006", "Егор", 0, 5_000);
        account.withdraw(3_000); // баланс -3000

        // Act
        account.deposit(1_000);

        // Assert
        assertEquals(-2_000, account.getBalance());
    }

    @Test
    void negativeCreditLimitIsRejected()
    {
        assertThrows(IllegalArgumentException.class,
                () -> new CreditAccount("C-007", "Жанна", 0, -1_000));
    }
}