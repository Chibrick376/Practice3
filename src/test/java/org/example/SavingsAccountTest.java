package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SavingsAccountTest
{

    @Test
    void cannotWithdrawBelowMinimumBalance()
    {
        // Arrange
        SavingsAccount account = new SavingsAccount("S-001", "Алиса", 10_000, 1_000);

        // Act
        boolean result = account.withdraw(9_500);

        // Assert
        assertFalse(result);
        assertEquals(10_000, account.getBalance());
    }

    @Test
    void canWithdrawDownToMinimumBalance()
    {
        // Arrange
        SavingsAccount account = new SavingsAccount("S-002", "Борис", 10_000, 1_000);

        // Act
        boolean result = account.withdraw(9_000);

        // Assert
        assertTrue(result);
        assertEquals(1_000, account.getBalance());
    }

    @Test
    void exampleFromTaskWorks()
    {
        // Arrange
        SavingsAccount account = new SavingsAccount("S-003", "Виктор", 10_000, 1_000);

        // Act
        boolean first = account.withdraw(8_500);
        boolean second = account.withdraw(1_000);

        // Assert
        assertTrue(first);
        assertFalse(second);
        assertEquals(1_500, account.getBalance());
    }

    @Test
    void cannotWithdrawNegativeAmount()
    {
        // Arrange
        SavingsAccount account = new SavingsAccount("S-004", "Галина", 10_000, 1_000);

        // Act
        boolean result = account.withdraw(-100);

        // Assert
        assertFalse(result);
        assertEquals(10_000, account.getBalance());
    }

    @Test
    void initialBalanceBelowMinimumIsRejected()
    {
        assertThrows(IllegalArgumentException.class,
                () -> new SavingsAccount("S-005", "Дарья", 500, 1_000));
    }

    @Test
    void negativeMinimumBalanceIsRejected()
    {
        assertThrows(IllegalArgumentException.class,
                () -> new SavingsAccount("S-006", "Егор", 10_000, -500));
    }

    @Test
    void depositIncreasesBalance()
    {
        // Arrange
        SavingsAccount account = new SavingsAccount("S-007", "Жанна", 2_000, 1_000);

        // Act
        account.deposit(500);

        // Assert
        assertEquals(2_500, account.getBalance());
    }
}